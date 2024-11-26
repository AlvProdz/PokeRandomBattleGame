package com.example.pokerandombattle.services

import com.example.pokerandombattle.data.Pokemon
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeAPIService {
    @GET("pokemon/{id}")
    suspend fun getPokemonByID(@Path("id") id: String) : Pokemon
}
