package com.example.pokedexkotlinsample.data.api.response.stats

import com.google.gson.annotations.SerializedName

data class GenerationVi(
    @SerializedName("omegaruby-alphasapphire") val omegaruby_alphasapphire: OmegarubyAlphasapphire,
    @SerializedName("x-y") val x_y: XY
)