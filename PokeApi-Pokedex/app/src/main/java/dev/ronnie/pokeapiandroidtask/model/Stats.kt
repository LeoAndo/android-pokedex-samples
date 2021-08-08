package dev.ronnie.pokeapiandroidtask.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class Stats(
    val base_stat: Int,
    val effort: Int,
    val stat: Stat
) : Serializable