package com.example.pokedexkotlinsample.presentation.stats

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.pokedexkotlinsample.R
import com.example.pokedexkotlinsample.databinding.FragmentPokemonStatsBinding
import com.example.pokedexkotlinsample.presentation.adapter.ImageLoadable
import com.example.pokedexkotlinsample.presentation.adapter.StatsAdapter
import com.example.pokedexkotlinsample.presentation.util.OnRetryConnectionListener
import com.example.pokedexkotlinsample.presentation.util.PokemonExceptionHandler
import com.example.pokedexkotlinsample.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonStatsFragment : Fragment(R.layout.fragment_pokemon_stats), ImageLoadable {

    private val binding by viewBindings(FragmentPokemonStatsBinding::bind)
    private lateinit var adapter: StatsAdapter
    private val args by navArgs<PokemonStatsFragmentArgs>()
    private val viewModel: PokemonStatsViewModel by viewModels()
    private val exceptionHandler =
        PokemonExceptionHandler(fragment = this, onUnAuthorizedAction = {})

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pokemon = args.pokemon
        adapter = StatsAdapter()

        binding.apply {
            // set Name.
            pokemonItemName.text = pokemon.idWithName
            // load pokemon image.
            pokemonItemImage.loadImage(imageUrl = pokemon.pictureUrl, fitCenter = true)
        }

        getPokemonStats()

        viewModel.pokemonStatsModel.observe(viewLifecycleOwner) { model ->
            binding.progressCircular.isVisible = false
            binding.apply {
                pokemonItemWeight.text = model.weight
                pokemonItemHeight.text = model.height
                pokemonStatList.adapter = adapter
                adapter.setStats(model.stats.toMutableList())
            }
        }

        exceptionHandler.setOnRetryConnectionListener(object : OnRetryConnectionListener {
            override fun onRetry() {
                getPokemonStats()
            }
        })

    }

    private fun getPokemonStats() {
        viewLifecycleOwner.lifecycleScope.launch(exceptionHandler.coroutineExceptionHandler) {
            binding.progressCircular.isVisible = true
            viewModel.getPokemonStats(args.pokemon.id)
        }
    }
}