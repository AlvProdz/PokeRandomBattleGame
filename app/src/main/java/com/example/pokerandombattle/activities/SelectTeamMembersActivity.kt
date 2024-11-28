package com.example.pokerandombattle.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokerandombattle.R
import com.example.pokerandombattle.adapters.PokemonAdapter
import com.example.pokerandombattle.data.Player
import com.example.pokerandombattle.data.Pokemon
import com.example.pokerandombattle.data.providers.PlayerDAO
import com.example.pokerandombattle.databinding.ActivitySelectTeamMembersBinding
import com.example.pokerandombattle.utils.RetrofitManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectTeamMembersActivity : AppCompatActivity() {
    private val service = RetrofitManager.getRetrofit()
    private var playerDAO: PlayerDAO = PlayerDAO(this)
    private lateinit var binding: ActivitySelectTeamMembersBinding
    private lateinit var adapter: PokemonAdapter
    private lateinit var player: Player
    private var pokemonList: List<Pokemon> = emptyList()

    companion object {
        const val PARAM_POKEMON_ID = "POKEMON_ID"
        const val PARAM_PLAYER_ID = "PLAYER_ID"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectTeamMembersBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        adapter = PokemonAdapter(pokemonList) { pokemonItem ->
            goToCombat(pokemonItem)
        }
        binding.recyclerViewSelectableTeamMembers.adapter = adapter
        binding.recyclerViewSelectableTeamMembers.layoutManager = GridLayoutManager(this, 3)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val id = intent.getLongExtra(PARAM_PLAYER_ID,-1)
        getPlayer(id)
        generatePokemonTeam()
    }

    private fun generatePokemonTeam(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val pokemonListGenerated: MutableList<Pokemon> = mutableListOf()
                for(index in 1..9){
                    val resultPokemon = service.getPokemonByID((1..150).random().toString())
                    for (type in resultPokemon.types){
                        val resultTypeDamageRelation = service.getTypeDamageRelationByTypeName(type.typeData.name)
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

    private fun getPlayer(id: Long) {
                player = playerDAO.getPlayerByID(id)!!
                CoroutineScope(Dispatchers.Main).launch {
                    println(player)
                }
    }

    private fun goToCombat(pokemon: Pokemon, player: Player = this.player){
        val intent = Intent(this, CombatActivity::class.java)
        intent.putExtra(CombatActivity.PARAM_POKEMON_ID, pokemon.id)
        intent.putExtra(CombatActivity.PARAM_PLAYER_ID, player.id)
        startActivity(intent)

    }
}