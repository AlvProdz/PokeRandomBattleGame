package com.example.pokerandombattle.activities

import android.content.Intent
import android.graphics.Color
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
    private var resultMatch: String = ""

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
        binding.touchToContinue.setOnClickListener {
            when(stateCombat){
                "YOUR_POKEMON" -> {
                    stateCombat = "ENEMY_POKEMON"
                    getEnemyPokemon()
                    changeButtonColorToRedAndTextToFight()
                }
                "ENEMY_POKEMON" -> {
                    stateCombat = "FIGHTING"
                    resultMatch = pokemonFight(pokemon, enemyPokemon)
                    changeButtonToContinue()
                }
                "FIGHTING" -> {
                    stateCombat = "RESULTS"
                    setResultAndFinishGame(resultMatch)
                }
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
                for (type in pokemon.types){
                    val resultTypeDamageRelation = service.getTypeDamageRelationByTypeName(type.typeData.name)
                    pokemon.damageRelations = resultTypeDamageRelation.damageRelations
                }
                CoroutineScope(Dispatchers.Main).launch {
                    displayMyPokemonInfo()
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
                for (type in enemyPokemon.types){
                    val resultTypeDamageRelation = service.getTypeDamageRelationByTypeName(type.typeData.name)
                    enemyPokemon.damageRelations = resultTypeDamageRelation.damageRelations
                }
                CoroutineScope(Dispatchers.Main).launch {
                    displayEnemyPokemonInfo()
                }
            } catch (e: Exception) {
                Log.e("API", e.stackTraceToString())
            }
        }
    }

    private fun displayMyPokemonInfo(){
        binding.combatTextViewLog.text = buildString {
            append((pokemon.name).replaceFirstChar { it.titlecase() })
            append(getString(R.string.yourPokemonAnnounce))
            Picasso.get().load(pokemon.sprites.backImgURL).into(binding.myPokemonIMG)
            stateCombat = "YOUR_POKEMON"
        }
    }

    private fun displayEnemyPokemonInfo(){
        binding.combatTextViewLog.text = buildString {
            append(getString(R.string.enemyPokemonAnnounce))
            append((enemyPokemon.name).replaceFirstChar { it.titlecase() })
        }
        Picasso.get().load(enemyPokemon.sprites.otherSprites.officialArtWork.frontImgURL).into(binding.enemyPokemonIMG)
    }

    private fun changeButtonColorToRedAndTextToFight(){

        binding.touchToContinue.setBackgroundColor(Color.parseColor("#E4080A"))
        binding.touchToContinue.text = getString(R.string.fight)
        binding.touchToContinue.textSize = 30.0F
    }

    private fun changeButtonToContinue(){
        binding.touchToContinue.setBackgroundColor(Color.parseColor("#0768B7"))
        binding.touchToContinue.text = getString(R.string.touchToContinue)
        binding.touchToContinue.textSize = 20.0F
    }

    private fun pokemonFight(pokemon: Pokemon, enemyPokemon: Pokemon): String{

        val result: String
        val myPokemonTypesList = pokemon.types.map { it.typeData.name }
        val myPokemonVulnerableTypes = pokemon.damageRelations.vulnerabilities.map { it.name }
        val myPokemonBadAgainstTypes = pokemon.damageRelations.badAgainstList.map { it.name }
        val myPokemonImmuneAgainstTypes = pokemon.damageRelations.immuneToList.map { it.name }

        val enemyPokemonTypesList = enemyPokemon.types.map { it.typeData.name }
        val enemyPokemonVulnerableTypes = enemyPokemon.damageRelations.vulnerabilities.map { it.name }
        val enemyPokemonBadAgainstTypes = enemyPokemon.damageRelations.badAgainstList.map { it.name }
        val enemyPokemonImmuneAgainstTypes = enemyPokemon.damageRelations.immuneToList.map { it.name }

        val myPokemonPoints = calculatePointsMyPokemon(myPokemonTypesList,enemyPokemonVulnerableTypes,enemyPokemonBadAgainstTypes, enemyPokemonImmuneAgainstTypes )
        val enemyPokemonPoints = calculatePointsEnemyPokemon(enemyPokemonTypesList,myPokemonVulnerableTypes, myPokemonBadAgainstTypes, myPokemonImmuneAgainstTypes)

        result = if(myPokemonPoints > enemyPokemonPoints){
            getString(R.string.victory)
        }else if (myPokemonPoints < enemyPokemonPoints){
            getString(R.string.defeat)
        }else{
            getString(R.string.draw)
        }

        println(myPokemonPoints)
        println(enemyPokemonPoints)
        println(result)

        when(result){
            getString(R.string.victory) -> binding.combatTextViewLog.text = buildString {
                append((pokemon.name).replaceFirstChar { it.titlecase() })
                append(getString(R.string.yourPokemonWonAnnounce))
                append((enemyPokemon.name).replaceFirstChar { it.titlecase() })
                append(getString(R.string.yourPokemonWonReason))
            }
            getString(R.string.defeat) -> binding.combatTextViewLog.text = buildString {
                append((pokemon.name).replaceFirstChar { it.titlecase() })
                append(getString(R.string.yourPokemonLostAnnounce))
                append((enemyPokemon.name).replaceFirstChar { it.titlecase() })
                append(getString(R.string.yourPokemonLostReason))
            }
            getString(R.string.draw) -> binding.combatTextViewLog.text = buildString {
                append(getString(R.string.DrawAnnounceReason))
            }
        }
        return result
    }

    private fun calculatePointsMyPokemon(myPokemonTypesList: List<String>, enemyPokemonVulnerableTypes: List<String>, enemyPokemonBadAgainstTypes: List<String>, enemyPokemonImmuneAgainstTypes: List<String>): Int {
        var finalMyPokemonPoints = 0
        for(type in myPokemonTypesList){
            if(enemyPokemonVulnerableTypes.contains(type)){
                finalMyPokemonPoints += 10
            }
            if(enemyPokemonBadAgainstTypes.contains(type)){
                finalMyPokemonPoints += 5
            }
            if(enemyPokemonImmuneAgainstTypes.contains(type)){
                finalMyPokemonPoints += 0
            }
        }
        return finalMyPokemonPoints
    }

    private fun calculatePointsEnemyPokemon(enemyPokemonTypesList: List<String>, myPokemonVulnerableTypes: List<String>, myPokemonBadAgainstTypes: List<String>, myPokemonImmuneAgainstTypes: List<String> ): Int {
        var finalEnemyPokemonPoints = 0
        for(type in enemyPokemonTypesList){
            if(myPokemonVulnerableTypes.contains(type)){
                finalEnemyPokemonPoints += 10
            }
            if(myPokemonBadAgainstTypes.contains(type)){
                finalEnemyPokemonPoints += 5
            }
            if(myPokemonImmuneAgainstTypes.contains(type)){
                finalEnemyPokemonPoints += 0
            }
        }
        return finalEnemyPokemonPoints
    }

    private fun setResultAndFinishGame(result: String){

        when(result){
            getString(R.string.victory) -> player.victories += 1
            getString(R.string.defeat) -> player.defeats += 1
        }
        playerDAO.update(player)

        val intent = Intent(this, MainMenu::class.java)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}