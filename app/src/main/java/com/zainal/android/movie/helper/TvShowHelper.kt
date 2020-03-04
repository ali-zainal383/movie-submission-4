package com.zainal.android.movie.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zainal.android.movie.db.DatabaseContract.FavoriteTvShow.Companion.TABLE_NAME
import com.zainal.android.movie.db.DatabaseContract.FavoriteTvShow.Companion._ID
import com.zainal.android.movie.db.DatabaseHelper
import java.sql.SQLException

class TvShowHelper(context: Context)  {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: TvShowHelper? = null
        private lateinit var dataBaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context) : TvShowHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = TvShowHelper(context)
                    }
                }
            }
            return INSTANCE as TvShowHelper
        }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(DATABASE_TABLE,null,null,null,null,null,"$_ID ASC")
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = $id", null)
    }

    @SuppressLint("Recycle")
    fun checkFavorite(id: Int?) : Boolean {
        database = dataBaseHelper.readableDatabase
        val query = database.rawQuery("SELECT * FROM $DATABASE_TABLE WHERE $_ID = $id", null)
        return query.count > 0
    }
}