package com.example.pokerandombattle.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pokerandombattle.R
import com.example.pokerandombattle.data.Player
import com.example.pokerandombattle.data.providers.PlayerDAO
import com.example.pokerandombattle.databinding.ActivityMainBinding
import java.lang.String


class MainMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var playerDAO: PlayerDAO = PlayerDAO(this)
    private var playerList : List<Player> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadDBData()

        binding.profile1Button.setOnClickListener {
            if(playerList[0].initiated == 0) {
                newPlayerName(playerList[0])
            }
            else{
                playGame(playerList[0])
            }
        }
        binding.profile2Button.setOnClickListener {
            if(playerList[1].initiated == 0) {
                newPlayerName(playerList[1])
            }
            else{
                playGame(playerList[1])
            }
        }
        binding.profile3Button.setOnClickListener {
            if(playerList[2].initiated == 0) {
                newPlayerName(playerList[2])
            }
            else{
                playGame(playerList[2])
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updatePlayerListOnResume()
    }

    private fun getPlayers(): List<Player>{
        val playerListTemp = playerDAO.getAllPlayers()
        return playerListTemp
    }

    private fun loadDBData(){
        playerList = getPlayers()
        binding.profile1Button.text = playerList[0].name
        binding.profile2Button.text = playerList[1].name
        binding.profile3Button.text = playerList[2].name
    }

    private fun newPlayerName(player: Player){
        val playerEditText = EditText(this)
        val dialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("Create player name")
            .setView(playerEditText)
            .setPositiveButton("Save") { _, _ ->
                player.name = String.valueOf(playerEditText.text)
                playerDAO.update(player)
                playGame(player)
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun playGame(player: Player) {
        val intent = Intent(this, SelectTeamMembersActivity::class.java)
        intent.putExtra(SelectTeamMembersActivity.PARAM_PLAYER_ID, player.id)
        startActivity(intent)
    }

    private fun updatePlayerListOnResume(){
        loadDBData()
    }

}