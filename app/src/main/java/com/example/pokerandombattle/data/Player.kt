package com.example.pokerandombattle.data

data class Player(
    val id: Long,
    var name: String,
    var initiated: Int,
    var victories: Int,
    var defeats: Int
)
