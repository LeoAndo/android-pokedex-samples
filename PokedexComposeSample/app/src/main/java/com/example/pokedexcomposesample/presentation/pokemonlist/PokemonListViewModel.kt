package com.example.pokedexcomposesample.presentation.pokemonlist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexcomposesample.data.api.response.toModels
import com.example.pokedexcomposesample.data.repository.PAGE_SIZE
import com.example.pokedexcomposesample.data.repository.PokemonRepository
import com.example.pokedexcomposesample.domain.model.PokemonModel
import com.example.pokedexkotlinsample.domain.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 0

    var pokemons = mutableStateOf<List<PokemonModel>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokemonModel>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }

    fun searchPokemonList(query: String) {
        val listToSearch = if (isSearchStarting) {
            pokemons.value
        } else {
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokemons.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.name.contains(
                    query.trim(),
                    ignoreCase = true
                ) || it.id.toString() == query.trim()
            }
            if (isSearchStarting) {
                cachedPokemonList = pokemons.value
                isSearchStarting = false
            }
            pokemons.value = results
            isSearching.value = true
        }
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = repository.getPokemons(PAGE_SIZE, curPage * PAGE_SIZE)) {
                is ApiResult.Success -> {
                    endReached.value = (curPage * PAGE_SIZE >= result.value.count)
                    pokemons.value = result.value.results.toModels()
                    curPage++
                    loadError.value = ""
                    isLoading.value = false
                }
                is ApiResult.Failure -> {
                    loadError.value = result.errorBody?.toString() ?: "error"
                }
                is ApiResult.NetworkError -> {
                    loadError.value = "NetworkError!"
                }
            }
        }
    }
}