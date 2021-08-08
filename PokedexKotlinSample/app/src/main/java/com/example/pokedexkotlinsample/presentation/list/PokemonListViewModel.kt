package com.example.pokedexkotlinsample.presentation.list

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokedexkotlinsample.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pokemons = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty() },
        savedStateHandle.getLiveData(KEY_QUERY, DEFAULT_QUERY_TEXT).asFlow()
            .flatMapLatest { query ->
                Log.d(LOG_TAG, "query: $query")
                pokemonRepository.getPokemons(query)
            }.cachedIn(viewModelScope)
    ).flattenMerge(2)


    init {
        if (!savedStateHandle.contains(KEY_QUERY)) {
            savedStateHandle.set(KEY_QUERY, DEFAULT_QUERY_TEXT)
        }
    }

    fun searchPokemons(query: String) {
        if (savedStateHandle.get<String>(KEY_QUERY) != query) {
            savedStateHandle.set(KEY_QUERY, query)
        }
    }

    fun clearList() {
        clearListCh.trySend(Unit)
    }

    companion object {
        private const val KEY_QUERY = "current_query"
        private const val DEFAULT_QUERY_TEXT = ""
        private const val LOG_TAG = "PokemonListViewModel"
    }
}