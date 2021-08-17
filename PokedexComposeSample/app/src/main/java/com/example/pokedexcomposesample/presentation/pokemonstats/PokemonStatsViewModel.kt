package com.example.pokedexcomposesample.presentation.pokemonstats

import androidx.lifecycle.ViewModel
import com.example.pokedexcomposesample.data.api.response.stats.toModel
import com.example.pokedexcomposesample.data.repository.PokemonRepository
import com.example.pokedexkotlinsample.domain.ApiResult
import com.example.pokedexcomposesample.domain.model.PokemonStatsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokemonStatsViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    suspend fun getPokemonStats(id: Int): PokemonStatsModel? {
        return when (val ret = repository.getPokemonStats(id)) {
            is ApiResult.Success -> {
                ret.value.toModel()
            }
            is ApiResult.Failure -> {
                Timber.e("errorBody: %s", ret.errorBody.toString())
                null
            }
            ApiResult.NetworkError -> {
                Timber.e("NetworkError")
                null
            }
            else -> null
        }
    }
}