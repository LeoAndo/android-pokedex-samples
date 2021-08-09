package com.example.pokedexkotlinsample.domain.model

import com.example.pokedexkotlinsample.presentation.util.extractId
import com.example.pokedexkotlinsample.presentation.util.getGifUrl
import java.io.Serializable

data class PokemonResult(
    val name: String,
    val url: String
) : Serializable

fun PokemonResult.toPokemonModel(): PokemonModel {
    return PokemonModel(
        id = String.format("#%05d", url.extractId()),
        name = name.capitalize(),
        url = url,
        pictureUrl = url.getGifUrl(),
    )
}