package com.example.pokedexcomposesample.data.api.response.stats

import com.google.gson.annotations.SerializedName

data class Versions(
    @SerializedName("generation-i") val generation_i: GenerationI,
    @SerializedName("generation-ii") val generation_ii: GenerationIi,
    @SerializedName("generation-iii") val generation_iii: GenerationIii,
    @SerializedName("generation-iv") val generation_iv: GenerationIv,
    @SerializedName("generation-v") val generation_v: GenerationV,
    @SerializedName("generation-vi") val generation_vi: GenerationVi,
    @SerializedName("generation-vii") val generation_vii: GenerationVii,
    @SerializedName("generation-viii") val generation_viii: GenerationViii
)