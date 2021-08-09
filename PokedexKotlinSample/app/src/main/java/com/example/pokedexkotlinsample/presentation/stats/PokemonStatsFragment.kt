package com.example.pokedexkotlinsample.presentation.stats

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pokedexkotlinsample.R
import com.example.pokedexkotlinsample.databinding.FragmentPokemonStatsBinding
import com.example.pokedexkotlinsample.presentation.adapter.StatsAdapter
import com.example.pokedexkotlinsample.presentation.util.OnRetryConnectionListener
import com.example.pokedexkotlinsample.presentation.util.PokemonExceptionHandler
import com.example.pokedexkotlinsample.utils.viewBindings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonStatsFragment : Fragment(R.layout.fragment_pokemon_stats) {

    private val binding by viewBindings(FragmentPokemonStatsBinding::bind)
    private val adapter = StatsAdapter()
    private val args by navArgs<PokemonStatsFragmentArgs>()
    private val viewModel: PokemonStatsViewModel by viewModels()
    private val exceptionHandler =
        PokemonExceptionHandler(fragment = this, onUnAuthorizedAction = {})

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setting the colors based on dominant colors
        if (args.dominantColor != 0) {
            binding.card.setBackgroundColor(args.dominantColor)
        }
        //load pic
        binding.apply {
            Glide.with(root)
                .load(args.pictureUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(object : DrawableImageViewTarget(pokemonItemImage) {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        super.onResourceReady(resource, transition)
                        if (resource is GifDrawable) {
                            resource.setLoopCount(3)
                        }
                    }
                })
        }
        getPokemonStats()
        viewModel.pokemonStatsResponse.observe(viewLifecycleOwner, { response ->
            binding.progressCircular.isVisible = false
            binding.apply {
                (response.weight.div(10.0).toString() + " kgs").also { weight ->
                    pokemonItemWeight.text = weight
                }
                (response.height.div(10.0).toString() + " metres").also { height ->
                    pokemonItemHeight.text = height
                }
                pokemonStatList.adapter = adapter
                adapter.setStats(response.stats.toMutableList())
            }
        })
        exceptionHandler.setOnRetryConnectionListener(object : OnRetryConnectionListener {
            override fun onRetry() {
                getPokemonStats()
            }
        })
    }

    private fun getPokemonStats() {
        viewLifecycleOwner.lifecycleScope.launch(exceptionHandler.coroutineExceptionHandler) {
            binding.progressCircular.isVisible = true
            viewModel.getPokemonStats(args.pokemonResult.url)
        }
    }

    companion object {
        private const val LOG_TAG = "PokemonStatsFragment"
    }
}