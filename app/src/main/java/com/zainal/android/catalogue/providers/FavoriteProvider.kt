package com.zainal.android.catalogue.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.zainal.android.catalogue.db.DatabaseContract.AUTHORITY
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.CONTENT_URI as tvShowContentUri
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.TABLE_NAME as tvShowTable
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.CONTENT_URI as movieContentUri
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.TABLE_NAME as movieTable
import com.zainal.android.catalogue.helper.MovieHelper
import com.zainal.android.catalogue.helper.TvShowHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val MOVIE = 10
        private const val MOVIE_ID = 11
        private const val TV_SHOW = 20
        private const val TV_SHOW_ID = 21

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        private lateinit var movieHelper: MovieHelper
        private lateinit var tvShowHelper: TvShowHelper

        init {
            sUriMatcher.addURI(AUTHORITY, movieTable, MOVIE)
            sUriMatcher.addURI(AUTHORITY, "${movieTable}/#", MOVIE_ID)
            sUriMatcher.addURI(AUTHORITY, tvShowTable, TV_SHOW)
            sUriMatcher.addURI(AUTHORITY, "${tvShowTable}/#", TV_SHOW_ID)
        }
    }

    override fun onCreate(): Boolean {
        movieHelper = MovieHelper.getInstance(context as Context)
        movieHelper.open()
        tvShowHelper = TvShowHelper.getInstance(context as Context)
        tvShowHelper.open()
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            MOVIE -> movieHelper.queryAll()
            MOVIE_ID -> movieHelper.queryById(uri.lastPathSegment.toString())
            TV_SHOW -> tvShowHelper.queryAll()
            TV_SHOW_ID -> tvShowHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var uriString = ""
        when(sUriMatcher.match(uri)) {
            MOVIE -> {
                val added: Long = movieHelper.insert(values)
                context?.contentResolver?.notifyChange(movieContentUri, null)
                uriString = "${movieContentUri}/$added"
            }
            TV_SHOW -> {
                val added: Long = tvShowHelper.insert(values)
                context?.contentResolver?.notifyChange(tvShowContentUri, null)
                uriString = "${tvShowContentUri}/$added"
            }
        }
        return Uri.parse(uriString)
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val updated: Int = when(sUriMatcher.match(uri)) {
            MOVIE_ID -> movieHelper.update(uri.lastPathSegment.toString(), values)
            TV_SHOW_ID -> tvShowHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }
        when(sUriMatcher.match(uri)) {
            MOVIE_ID -> context?.contentResolver?.notifyChange(movieContentUri, null)
            TV_SHOW_ID -> context?.contentResolver?.notifyChange(tvShowContentUri, null)
        }
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (sUriMatcher.match(uri)) {
            MOVIE_ID -> movieHelper.deleteById(uri.lastPathSegment.toString())
            TV_SHOW_ID -> tvShowHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        when(sUriMatcher.match(uri)){
            MOVIE_ID -> context?.contentResolver?.notifyChange(movieContentUri, null)
            TV_SHOW_ID -> context?.contentResolver?.notifyChange(tvShowContentUri, null)
        }
        return deleted
    }
}
