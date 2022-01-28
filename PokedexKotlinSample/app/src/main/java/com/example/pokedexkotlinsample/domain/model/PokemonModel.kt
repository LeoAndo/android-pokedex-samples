package com.example.pokedexkotlinsample.domain.model

import java.io.Serializable

data class PokemonModel(
    val id: Int,
    val name: String,
    val idWithName: String, // 表示用
    val url: String,
    val pictureUrl: String
) : Serializable