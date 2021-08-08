package dev.ronnie.pokeapiandroidtask.model

import java.io.Serializable

data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResult>
) : Serializable