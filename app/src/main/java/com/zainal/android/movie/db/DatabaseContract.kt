package com.zainal.android.movie.db

import android.provider.BaseColumns

class DatabaseContract {

    internal class FavoriteMovie : BaseColumns {
        companion object {
            const val TABLE_NAME = "movie"
            const val _ID = "_id"
            const val ORIGINAL_TITLE = "original_title"
            const val OVERVIEW = "overview"
            const val RELEASE_DATE = "release_date"
            const val POSTER = "poster"
            const val BACKDROP = "backdrop"
        }
    }

    internal class FavoriteTvShow : BaseColumns {
        companion object {
            const val TABLE_NAME = "tv_show"
            const val _ID = "_id"
            const val ORIGINAL_TITLE = "original_title"
            const val OVERVIEW = "overview"
            const val RELEASE_DATE = "release_date"
            const val POSTER = "poster"
            const val BACKDROP = "backdrop"
        }
    }
}