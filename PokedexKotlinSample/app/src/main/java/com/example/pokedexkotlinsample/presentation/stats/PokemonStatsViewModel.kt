package com.example.pokedexkotlinsample.presentation.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedexkotlinsample.data.api.PokemonService
import com.example.pokedexkotlinsample.data.api.response.stats.PokemonStatsResponse
import com.example.pokedexkotlinsample.data.apiCall
import com.example.pokedexkotlinsample.presentation.extractId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonStatsViewModel @Inject constructor(private val api: PokemonService) :
    ViewModel() {
    private val _pokemonStatsResponse = MutableLiveData<PokemonStatsResponse>()
    val pokemonStatsResponse: LiveData<PokemonStatsResponse> = _pokemonStatsResponse
    suspend fun getPokemonStats(url: String) {
        val id = url.extractId()
        val ret = apiCall { api.getPokemonStats(id) }
        _pokemonStatsResponse.value = ret // TODO to Model (from Repository)
    }
}