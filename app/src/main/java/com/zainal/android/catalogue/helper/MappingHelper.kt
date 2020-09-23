package com.zainal.android.catalogue.helper

import android.database.Cursor
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.BACKDROP as movieBackdrop
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.POSTER as moviePoster
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.RELEASE_DATE as movieRelease
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.OVERVIEW as movieOverview
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.ORIGINAL_TITLE as movieTitle
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion._ID as movieId
import com.zainal.android.catalogue.model.Movie
import com.zainal.android.catalogue.model.TvShow
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion._ID as tvShowId
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.ORIGINAL_TITLE as tvShowTitle
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.OVERVIEW as tvShowOverview
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.RELEASE_DATE as tvShowRelease
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.POSTER as tvShowPoster
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.BACKDROP as tvShowBackdrop

object MappingHelper {

    fun mapMovieCursorToArrayList(movieCursor: Cursor?): ArrayList<Movie> {
        val movieList = ArrayList<Movie>()
        movieCursor?.apply {
            while (moveToNext()) {
                val id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(movieId))
                val title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(movieTitle))
                val release = movieCursor.getString(movieCursor.getColumnIndexOrThrow(movieRelease))
                val overview = movieCursor.getString(movieCursor.getColumnIndexOrThrow(movieOverview))
                val poster = movieCursor.getString(movieCursor.getColumnIndexOrThrow(moviePoster))
                val backdrop = movieCursor.getString(movieCursor.getColumnIndexOrThrow(movieBackdrop))
                movieList.add(Movie(id, title, release, overview, poster, backdrop))
            }
        }
        return movieList
    }

    fun mapTvShowCursorToArrayList(tvShowCursor: Cursor?): ArrayList<TvShow> {
        val tvShowList = ArrayList<TvShow>()
        tvShowCursor?.apply {
            while (moveToNext()) {
                val id = tvShowCursor.getInt(tvShowCursor.getColumnIndexOrThrow(tvShowId))
                val title = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(tvShowTitle))
                val release = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(tvShowRelease))
                val overview = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(tvShowOverview))
                val poster = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(tvShowPoster))
                val backdrop = tvShowCursor.getString(tvShowCursor.getColumnIndexOrThrow(tvShowBackdrop))
                tvShowList.add(TvShow(id, title, release, overview, poster, backdrop))
            }
        }
        return tvShowList
    }
}