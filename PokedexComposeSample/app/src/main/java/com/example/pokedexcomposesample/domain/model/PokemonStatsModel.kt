package com.example.pokedexkotlinsample.domain.model

import com.example.pokedexcomposesample.data.api.response.stats.Stat

data class PokemonStatsModel(
    val weight: String,
    val height: String,
    val stats: List<Stat>
)