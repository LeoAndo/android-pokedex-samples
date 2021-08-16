package com.example.pokedexcomposesample.domain.model

import androidx.annotation.ColorInt
import java.io.Serializable

data class PokemonModel(
    val id: Int,
    val name: String,
    val idWithName: String, // 表示用
    val url: String,
    @ColorInt var dominantColor: Int = 0,
    val pictureUrl: String
) : Serializable