package com.example.pokerandombattle.data.providers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.pokerandombattle.data.Player
import com.example.pokerandombattle.utils.DatabaseManager

class PlayerDAO(private val context: Context){
    private lateinit var db: SQLiteDatabase

    private fun openDatabase() {
        db = DatabaseManager(context).writableDatabase
    }

    private fun closeDatabase() {
        db.close()
    }

    fun update(player: Player) {
        openDatabase()
        val values = ContentValues().apply {
            put("name", player.name)
            put("initiated", 1)
            put("victories", player.victories)
            put("defeats", player.defeats)
        }
        try {
            val updatedRows = db.update("Player", values, "id = ${player.id}", null)
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            closeDatabase()
        }
    }

    fun getPlayerByID(id: Long) : Player? {
        openDatabase()
        val projection = arrayOf("id", "name", "victories", "defeats")

        try {
            val cursor = db.query(
                "Player",                    // The table to query
                projection,                         // The array of columns to return (pass null to get all)
                "id = $id",  // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                       // don't group the rows
                null,                         // don't filter by row groups
                null                         // The sort order
            )

            if (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val initiated = cursor.getInt(cursor.getColumnIndexOrThrow("initiated"))
                val victories = cursor.getInt(cursor.getColumnIndexOrThrow("victories"))
                val defeats = cursor.getInt(cursor.getColumnIndexOrThrow("defeats"))

                return Player(id, name, initiated, victories, defeats)
            }
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            closeDatabase()
        }
        return null
    }

    fun getAllPlayers() : List<Player> {
        openDatabase()

        var list: MutableList<Player> = mutableListOf()

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf("id", "name", "initiated", "victories", "defeats")

        try {
            val cursor = db.query(
                "Player",                    // The table to query
                projection,                         // The array of columns to return (pass null to get all)
                null,                       // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                       // don't group the rows
                null,                         // don't filter by row groups
                null                         // The sort order
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val initiated = cursor.getInt(cursor.getColumnIndexOrThrow("initiated"))
                val victories = cursor.getInt(cursor.getColumnIndexOrThrow("victories"))
                val defeats = cursor.getInt(cursor.getColumnIndexOrThrow("defeats"))

                val player = Player(id, name, initiated, victories, defeats)
                list.add(player)
            }
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            closeDatabase()
        }
        return list
    }


}