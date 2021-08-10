package com.example.pokedexkotlinsample.domain.model

import com.example.pokedexkotlinsample.presentation.util.extractId
import com.example.pokedexkotlinsample.presentation.util.getGifUrl
import java.io.Serializable

data class PokemonResult(
    val name: String,
    val url: String
) : Serializable

fun List<PokemonResult>.toModels(): List<PokemonModel> {
    return this.map { result ->
        PokemonModel(
            id = result.url.extractId(),
            name = result.name,
            idWithName = String.format(
                "#%05d",
                result.url.extractId()
            ) + "\n" + result.name.capitalize(),
            url = result.url,
            pictureUrl = result.url.getGifUrl(),
        )
    }
}