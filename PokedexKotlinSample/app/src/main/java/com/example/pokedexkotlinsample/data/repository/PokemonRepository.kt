package com.example.pokedexkotlinsample.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.pokedexkotlinsample.data.api.PokemonService
import com.example.pokedexkotlinsample.data.datasource.remote.PokemonDataSource
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val api: PokemonService
) {
    /**
     * @param query If empty, search all pokemon`s
     */
    fun getPokemons(query: String) = Pager(
        config = PagingConfig(
            enablePlaceholders = false,
            pageSize = LOAD_PAGE_SIZE,
            initialLoadSize = LOAD_PAGE_SIZE
        ),
        pagingSourceFactory = {
            PokemonDataSource(api, query)
        }
    ).flow

    companion object {
        private const val LOAD_PAGE_SIZE = 100 // Page size to load at one time
    }
}