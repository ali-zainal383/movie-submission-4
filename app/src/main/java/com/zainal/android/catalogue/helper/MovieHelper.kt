package com.zainal.android.catalogue.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.TABLE_NAME
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion._ID
import com.zainal.android.catalogue.db.DatabaseHelper
import java.sql.SQLException

class MovieHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: MovieHelper? = null
        private lateinit var database: SQLiteDatabase
        private lateinit var dataBaseHelper: DatabaseHelper

        fun getInstance(context: Context) : MovieHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = MovieHelper(context)
                    }
                }
            }
            return INSTANCE as MovieHelper
        }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun queryAll(): Cursor {
        return database.query(DATABASE_TABLE,null,
            null,null,null,null,"$_ID ASC")
    }

    fun queryById(id: String): Cursor {
        return database.query(DATABASE_TABLE,null,
            "$_ID = ?", arrayOf(id),null,null,null,null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?) : Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = $id", null)
    }
}