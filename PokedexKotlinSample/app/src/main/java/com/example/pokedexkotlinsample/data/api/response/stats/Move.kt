package com.example.pokedexkotlinsample.data.api.response.stats

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)