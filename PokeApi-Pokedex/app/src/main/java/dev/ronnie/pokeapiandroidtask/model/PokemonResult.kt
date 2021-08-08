package dev.ronnie.pokeapiandroidtask.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class PokemonResult(
    val name: String,
    val url: String
) : Serializable