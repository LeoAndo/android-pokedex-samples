package dev.ronnie.pokeapiandroidtask.model

import java.io.Serializable

data class PokemonResult(
    val name: String,
    val url: String
) : Serializable