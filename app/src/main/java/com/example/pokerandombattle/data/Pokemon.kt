package com.example.pokerandombattle.data
import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("id") val id: String,
    @SerializedName ("name") var name: String,
    //@SerializedName ("") var image: Image,
    //@SerializedName ("") var pokemonType: PokemonType,
    //@SerializedName ("") var vulnerality: Vulnerality
)