package com.example.pokedexkotlinsample.data.api.response

import com.example.pokedexkotlinsample.domain.model.PokemonResult
import java.io.Serializable

data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResult>
) : Serializable