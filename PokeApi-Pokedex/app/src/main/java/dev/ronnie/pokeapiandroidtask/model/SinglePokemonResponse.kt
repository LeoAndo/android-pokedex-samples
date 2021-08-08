package dev.ronnie.pokeapiandroidtask.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class SinglePokemonResponse(
    val sprites: Sprites,
    val stats: List<Stats>,
    val height: Int,
    val weight: Int
) : Serializable