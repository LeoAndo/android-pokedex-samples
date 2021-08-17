package com.example.pokedexcomposesample.data.api.response

import com.example.pokedexcomposesample.domain.model.PokemonModel
import com.example.pokedexcomposesample.data.extractId
import com.example.pokedexcomposesample.data.getPictureUrl
import java.io.Serializable
import java.util.*

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
            ) + "\n" + result.name.capitalize(Locale.ROOT),
            url = result.url,
            pictureUrl = result.url.getPictureUrl(),
        )
    }
}