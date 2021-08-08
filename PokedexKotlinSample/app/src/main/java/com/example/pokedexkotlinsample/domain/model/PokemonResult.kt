package com.example.pokedexkotlinsample.domain.model

import java.io.Serializable

data class PokemonResult(
    val name: String,
    val url: String
) : Serializable