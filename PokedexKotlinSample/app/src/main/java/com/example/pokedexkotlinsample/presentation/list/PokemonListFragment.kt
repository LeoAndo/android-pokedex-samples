package com.example.pokedexkotlinsample.presentation.list

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokedexkotlinsample.R
import com.example.pokedexkotlinsample.databinding.FragmentPokemonListBinding
import com.example.pokedexkotlinsample.domain.model.PokemonModel
import com.example.pokedexkotlinsample.presentation.adapter.AppLoadStateAdapter
import com.example.pokedexkotlinsample.presentation.adapter.PokemonAdapter
import com.example.pokedexkotlinsample.presentation.adapter.PokemonAdapter.Companion.PRODUCT_VIEW_TYPE
import com.example.pokedexkotlinsample.presentation.util.OnRetryConnectionListener
import com.example.pokedexkotlinsample.presentation.util.PokemonExceptionHandler
import com.example.pokedexkotlinsample.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonListFragment : Fragment(R.layout.fragment_pokemon_list) {
    private val binding by viewBindings(FragmentPokemonListBinding::bind)
    private val viewModel: PokemonListViewModel by viewModels()
    private val adapter =
        PokemonAdapter { model: PokemonModel ->
            navigate(model)
        }
    private val exceptionHandler =
        PokemonExceptionHandler(fragment = this, onUnAuthorizedAction = {})

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        observeFlowData()
        setupSearchView()
        exceptionHandler.setOnRetryConnectionListener(object : OnRetryConnectionListener {
            override fun onRetry() {
                adapter.refresh()
            }
        })
    }

    private fun setupListAdapter() {
        // addLoadStateListener
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.source.append.endOfPaginationReached && adapter.itemCount < 1
            ) {
                Toast.makeText(requireContext(), "empty list.", Toast.LENGTH_SHORT).show()
            }
            // paging3: error handling
            val errorState = when {
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                exceptionHandler.handleError(it.error)
            }
        }

        // setup RecyclerView
        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == PRODUCT_VIEW_TYPE) 1 else 2
            }
        }
        binding.pokemonList.also {
            it.layoutManager = layoutManager
            it.adapter = adapter.withLoadStateHeaderAndFooter(
                header = AppLoadStateAdapter { adapter.retry() },
                footer = AppLoadStateAdapter { adapter.retry() },
            )
        }

        // listen setOnRefresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun observeFlowData() {
        // observe loadStates
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }
        // observe pokemons data
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pokemons.collectLatest {
                adapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect {
                    binding.pokemonList.scrollToPosition(0)
                }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchPokemons(binding.searchView.text.toString().trim())
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun navigate(model: PokemonModel) {
        findNavController().navigate(
            PokemonListFragmentDirections.toPokemonStatsFragment(model)
        )
    }
}