package com.example.pokedexcomposesample.data.repository

import com.example.pokedexcomposesample.data.api.PokemonService
import com.example.pokedexcomposesample.data.api.response.PokemonResponse
import com.example.pokedexcomposesample.data.api.response.stats.PokemonStatsResponse
import com.example.pokedexcomposesample.data.safeApiCall
import com.example.pokedexkotlinsample.domain.ApiResult
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


const val PAGE_SIZE = 20
class PokemonRepository @Inject constructor(
    private val api: PokemonService
) {
    suspend fun getPokemons(limit: Int, offset: Int): ApiResult<PokemonResponse> {
        return safeApiCall { api.getPokemons(limit, offset) }
    }

    suspend fun getPokemonStats(id: Int): ApiResult<PokemonStatsResponse> {
        return safeApiCall { api.getPokemonStats(id) }
    }
}