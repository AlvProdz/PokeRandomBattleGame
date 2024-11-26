package com.example.pokerandombattle.utils

import com.example.pokerandombattle.services.PokeAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager {
    companion object {
        fun getRetrofit() : PokeAPIService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(PokeAPIService::class.java)
        }
    }
}
