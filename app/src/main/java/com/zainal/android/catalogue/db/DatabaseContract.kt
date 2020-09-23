package com.zainal.android.catalogue.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.zainal.android.catalogue"
    const val SCHEME = "content"

    internal class FavoriteMovie : BaseColumns {
        companion object {
            const val TABLE_NAME = "movie"
            const val _ID = "_id"
            const val ORIGINAL_TITLE = "original_title"
            const val OVERVIEW = "overview"
            const val RELEASE_DATE = "release_date"
            const val POSTER = "poster"
            const val BACKDROP = "backdrop"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
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

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}