package com.example.pokedexkotlinsample.presentation.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedexkotlinsample.data.repository.PokemonRepository
import com.example.pokedexkotlinsample.domain.model.PokemonStatsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonStatsViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {
    private val _pokemonStatsModel = MutableLiveData<PokemonStatsModel>()
    val pokemonStatsModel: LiveData<PokemonStatsModel> = _pokemonStatsModel
    suspend fun getPokemonStats(id: Int) {
        _pokemonStatsModel.value = repository.getPokemonStats(id)
    }
}