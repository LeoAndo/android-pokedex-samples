package dev.ronnie.pokeapiandroidtask.model

import java.io.Serializable

data class SinglePokemonResponse(
    val sprites: Sprites,
    val stats: List<Stats>,
    val height: Int,
    val weight: Int
) : Serializable