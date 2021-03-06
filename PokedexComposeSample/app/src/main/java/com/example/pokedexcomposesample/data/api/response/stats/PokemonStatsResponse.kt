package com.example.pokedexcomposesample.data.api.response.stats

import com.example.pokedexcomposesample.domain.model.PokemonStatsModel
import java.io.Serializable

data class PokemonStatsResponse(
    val abilities: List<Ability>,
    val base_experience: Int,
    val forms: List<Form>,
    val game_indices: List<GameIndice>,
    val height: Int,
    val held_items: List<Any>,
    val id: Int,
    val is_default: Boolean,
    val location_area_encounters: String,
    val moves: List<Move>,
    val name: String,
    val order: Int,
    val past_types: List<Any>,
    val species: Species,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val weight: Int
) : Serializable

fun PokemonStatsResponse.toModel(): PokemonStatsModel {
    return PokemonStatsModel(
        weight = (weight.div(10.0).toString() + " kgs"),
        height = (height.div(10.0).toString() + " metres"),
        stats = stats,
        sprites = sprites,
        types = types
    )
}