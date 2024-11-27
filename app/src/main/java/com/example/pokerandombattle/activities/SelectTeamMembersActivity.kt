package com.example.pokerandombattle.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokerandombattle.R
import com.example.pokerandombattle.adapters.PokemonAdapter
import com.example.pokerandombattle.data.Pokemon
import com.example.pokerandombattle.databinding.ActivitySelectTeamMembersBinding
import com.example.pokerandombattle.utils.RetrofitManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectTeamMembersActivity : AppCompatActivity() {
    private val service = RetrofitManager.getRetrofit()
    private lateinit var binding: ActivitySelectTeamMembersBinding
    private lateinit var adapter: PokemonAdapter
    private var pokemonList: List<Pokemon> = emptyList()

    companion object {
        const val PARAM_PLAYER_ID = "PLAYER_ID"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectTeamMembersBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        adapter = PokemonAdapter(pokemonList) { superHeroItem ->
            //navigateToDetail(superHeroItem)
        }
        binding.recyclerViewSelectableTeamMembers.adapter = adapter
        binding.recyclerViewSelectableTeamMembers.layoutManager = GridLayoutManager(this, 3)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        generatePokemonTeam()
    }

    private fun generatePokemonTeam(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var pokemonListGenerated: MutableList<Pokemon> = mutableListOf()
                for(index in 1..9){
                    var resultPokemon = service.getPokemonByID((1..150).random().toString())
                    for (type in resultPokemon.types){
                        var resultTypeDamageRelation = service.getTypeDamageRelationByTypeName(type.typeData.name)
                        resultPokemon.damageRelations = resultTypeDamageRelation.damageRelations
                    }
                    pokemonListGenerated.add(resultPokemon)
                }
                CoroutineScope(Dispatchers.Main).launch {
                    adapter.updateItems(pokemonListGenerated)
                }
            } catch (e: Exception) {
                Log.e("API", e.stackTraceToString())
            }
        }
    }
}