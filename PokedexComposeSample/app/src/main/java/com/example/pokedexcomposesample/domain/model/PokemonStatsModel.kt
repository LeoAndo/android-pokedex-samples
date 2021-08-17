package com.example.pokedexcomposesample.domain.model

import com.example.pokedexcomposesample.data.api.response.stats.Sprites
import com.example.pokedexcomposesample.data.api.response.stats.Stat
import com.example.pokedexcomposesample.data.api.response.stats.Type

data class PokemonStatsModel(
    val weight: String,
    val height: String,
    val stats: List<Stat>,
    val sprites: Sprites,
    val types: List<Type>
)