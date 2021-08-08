package dev.ronnie.pokeapiandroidtask.model

import java.io.Serializable

data class Sprites(
    val back_default: String,
    val back_shiny: String,
    val front_default: String,
    val front_shiny: String
) : Serializable