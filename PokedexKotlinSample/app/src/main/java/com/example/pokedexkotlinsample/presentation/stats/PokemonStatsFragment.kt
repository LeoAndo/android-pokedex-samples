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
    private val adapter = StatsAdapter()
    private val args by navArgs<PokemonStatsFragmentArgs>()
    private val viewModel: PokemonStatsViewModel by viewModels()
    private val exceptionHandler =
        PokemonExceptionHandler(fragment = this, onUnAuthorizedAction = {})

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pokemon = args.pokemon


        binding.apply {
            // set TextColor.
            if (pokemon.dominantColor != 0) {
                card.setBackgroundColor(pokemon.dominantColor)
                pokemonItemName.setTextColor(pokemon.dominantColor)
                pokemonItemHeight.setTextColor(pokemon.dominantColor)
                pokemonItemWeight.setTextColor(pokemon.dominantColor)
            }
            // set Name.
            pokemonItemName.text = pokemon.id + "\n" + pokemon.name
            // load pokemon image.
            pokemonItemImage.loadImage(imageUrl = pokemon.pictureUrl, fitCenter = true)
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
            viewModel.getPokemonStats(args.pokemon.url)
        }
    }
}