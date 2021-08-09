package com.example.pokedexkotlinsample.presentation.util

fun String.extractId() = this.substringAfter("pokemon").replace("/", "").toInt()

fun String.getPictureUrl(): String {
    val id = this.extractId()
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${id}.png"
}

fun String.getGifUrl(): String {
    val id = this.extractId()
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/${id}.gif"
}