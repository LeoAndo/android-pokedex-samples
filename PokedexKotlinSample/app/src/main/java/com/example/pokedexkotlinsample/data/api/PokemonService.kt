package com.example.pokedexkotlinsample.data.api

import com.example.pokedexkotlinsample.data.api.response.PokemonResponse
import com.example.pokedexkotlinsample.data.api.response.stats.PokemonStatsResponse
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