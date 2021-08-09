package com.example.pokedexkotlinsample.domain.model

import androidx.annotation.ColorInt
import java.io.Serializable

data class PokemonModel(
    val id: String,
    val name: String,
    val url: String,
    @ColorInt var dominantColor: Int = 0,
    val pictureUrl: String
) : Serializable