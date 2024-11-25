package com.example.pokerandombattle.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.ContextCompat.getString
import com.example.pokerandombattle.R

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "PRB.db"


        private const val SQL_CREATE_TABLE =
            "CREATE TABLE Player (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "victories INTEGER," +
                    "defeats INTEGER)"
        private const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS Player"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
        val values = ContentValues().apply {
            put("name", "New Player")
            put("victories", 0)
            put("defeats", 0)
        }
        val id = db.insert("Player", null, values)
        val id2 = db.insert("Player", null, values)
        val id3 = db.insert("Player", null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }

    private fun onDestroy(db: SQLiteDatabase) {
        db.execSQL(SQL_DELETE_TABLE)
    }
}
