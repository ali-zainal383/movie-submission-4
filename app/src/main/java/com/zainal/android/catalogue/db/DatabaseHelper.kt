package com.zainal.android.catalogue.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.BACKDROP as movieBackdrop
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.POSTER as moviePoster
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.RELEASE_DATE as movieRelease
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.OVERVIEW as movieOverview
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.ORIGINAL_TITLE as movieTitle
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion._ID as movieId
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.TABLE_NAME as movieTable

import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.TABLE_NAME as tvShowTable
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion._ID as tvShowId
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.ORIGINAL_TITLE as tvShowTitle
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.OVERVIEW as tvShowOverview
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.RELEASE_DATE as tvShowRelease
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.POSTER as tvShowPoster
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.BACKDROP as tvShowBackdrop

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "db_favorite"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_MOVIE = "CREATE TABLE $movieTable" +
                "(${movieId} INTEGER PRIMARY KEY," +
                "$movieTitle TEXT NOT NULL," +
                "$movieOverview TEXT NOT NULL," +
                "$movieRelease TEXT NOT NULL," +
                "$moviePoster TEXT NOT NULL," +
                "$movieBackdrop TEXT NOT NULL)"

        private const val SQL_CREATE_TABLE_TV = "CREATE TABLE $tvShowTable" +
                "(${tvShowId} INTEGER PRIMARY KEY," +
                "$tvShowTitle TEXT NOT NULL," +
                "$tvShowOverview TEXT NOT NULL," +
                "$tvShowRelease TEXT NOT NULL," +
                "$tvShowPoster TEXT NOT NULL," +
                "$tvShowBackdrop TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_MOVIE)
        db?.execSQL(SQL_CREATE_TABLE_TV)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $movieTable")
        db?.execSQL("DROP TABLE IF EXISTS $tvShowTable")
        onCreate(db)
    }
}