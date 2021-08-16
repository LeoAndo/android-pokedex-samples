package com.example.pokedexcomposesample.data.api

import com.example.pokedexcomposesample.data.api.response.PokemonResponse
import com.example.pokedexcomposesample.data.api.response.stats.PokemonStatsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon/")
    suspend fun getPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonResponse

    @GET("pokemon/{id}/")
    suspend fun getPokemonStats(
        @Path("id") id: Int
    ): PokemonStatsResponse
}