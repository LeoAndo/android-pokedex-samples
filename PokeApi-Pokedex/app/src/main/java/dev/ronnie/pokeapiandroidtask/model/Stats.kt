package dev.ronnie.pokeapiandroidtask.model

import java.io.Serializable

data class Stats(
    val base_stat: Int,
    val effort: Int,
    val stat: Stat
) : Serializable