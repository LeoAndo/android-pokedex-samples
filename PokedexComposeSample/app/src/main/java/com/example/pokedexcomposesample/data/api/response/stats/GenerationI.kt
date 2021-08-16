package com.example.pokedexcomposesample.data.api.response.stats

import com.google.gson.annotations.SerializedName

data class GenerationI(
    @SerializedName("red-blue") val red_blue: RedBlue,
    val yellow: Yellow
)