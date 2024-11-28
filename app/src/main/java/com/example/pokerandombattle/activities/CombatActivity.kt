package com.example.pokerandombattle.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pokerandombattle.R
import com.example.pokerandombattle.data.Player
import com.example.pokerandombattle.data.Pokemon
import com.example.pokerandombattle.data.providers.PlayerDAO
import com.example.pokerandombattle.databinding.ActivityCombatBinding
import com.example.pokerandombattle.utils.RetrofitManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CombatActivity : AppCompatActivity() {
    private val service = RetrofitManager.getRetrofit()
    private var playerDAO: PlayerDAO = PlayerDAO(this)
    private lateinit var binding: ActivityCombatBinding
    private lateinit var player: Player
    private lateinit var pokemon: Pokemon
    private lateinit var enemyPokemon: Pokemon
    private var stateCombat: String = "PREPARING"

    companion object {
        const val PARAM_PLAYER_ID = "PLAYER_ID"
        const val PARAM_POKEMON_ID = "POKEMON_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCombatBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getPlayer(intent.getLongExtra(SelectTeamMembersActivity.PARAM_PLAYER_ID,-1))
        binding.touchToContinue.setOnClickListener(){
            when(stateCombat){
                "YOUR_POKEMON" -> {
                    getEnemyPokemon()
                }
                "ENEMY_POKEMON" -> {}
            }
        }
        getPokemon((intent.getStringExtra(SelectTeamMembersActivity.PARAM_POKEMON_ID).toString()))
    }

    private fun getPlayer(id: Long) {
        player = playerDAO.getPlayerByID(id)!!
        println(player)
    }

    private fun getPokemon(pokemonId: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                pokemon = service.getPokemonByID(pokemonId)
                CoroutineScope(Dispatchers.Main).launch {
                    binding.combatTextViewLog.text = buildString {
                        append(getString(R.string.yourPokemonAnnounce))
                        append(pokemon.name)
                        Picasso.get().load(pokemon.sprites.backImgURL).into(binding.myPokemonIMG)
                        stateCombat = "YOUR_POKEMON"
                    }
                }
            } catch (e: Exception) {
                Log.e("API", e.stackTraceToString())
            }
        }
    }
    private fun getEnemyPokemon(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                enemyPokemon = service.getPokemonByID((1..150).random().toString())
                CoroutineScope(Dispatchers.Main).launch {
                    binding.combatTextViewLog.text = buildString {
                        append(getString(R.string.enemyPokemonAnnounce))
                        append(enemyPokemon.name)
                    }
                    Picasso.get().load(enemyPokemon.sprites.otherSprites.officialArtWork.frontImgURL).into(binding.enemyPokemonIMG)
                    stateCombat = "ENEMY_POKEMON"
                }
            } catch (e: Exception) {
                Log.e("API", e.stackTraceToString())
            }
        }
    }
}