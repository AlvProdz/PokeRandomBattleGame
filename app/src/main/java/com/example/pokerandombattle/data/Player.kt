package com.example.pokerandombattle.data

data class Player(
    val id: Long,
    var name: String,
    var victories: Int
) {
    companion object {
        const val TABLE_NAME = "Player"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_VICTORIES = "victories"
    }
}
