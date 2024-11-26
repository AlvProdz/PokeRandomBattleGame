package com.example.pokerandombattle.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pokerandombattle.R
import com.example.pokerandombattle.data.Pokemon
import com.example.pokerandombattle.databinding.ActivitySelectTeamMembersBinding
import com.example.pokerandombattle.utils.RetrofitManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectTeamMembersActivity : AppCompatActivity() {
    private val service = RetrofitManager.getRetrofit()
    private lateinit var binding: ActivitySelectTeamMembersBinding
    private var pokemonList: MutableList<Pokemon> = mutableListOf()

    companion object {
        const val PARAM_PLAYER_ID = "PLAYER_ID"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectTeamMembersBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        teamIDsGeneration()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun teamIDsGeneration(){
        searchPokemonByID((1..150).random().toString())
    }

    private fun searchPokemonByID(query: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var result = service.getPokemonByID(query)

                CoroutineScope(Dispatchers.Main).launch {
                    pokemonList.add(result)
                    println(pokemonList)
                }
            } catch (e: Exception) {
                Log.e("API", e.stackTraceToString())
            }
        }
    }
}