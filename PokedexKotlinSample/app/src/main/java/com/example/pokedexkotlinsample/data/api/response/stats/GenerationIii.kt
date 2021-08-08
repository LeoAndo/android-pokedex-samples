package com.example.pokedexkotlinsample.data.api.response.stats

import com.google.gson.annotations.SerializedName

data class GenerationIii(
    val emerald: Emerald,
    @SerializedName("firered-leafgreen") val firered_leafgreen: FireredLeafgreen,
    @SerializedName("ruby-sapphire") val ruby_sapphire: RubySapphire
)