package com.zainal.android.catalogue.widget

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.zainal.android.catalogue.R
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.CONTENT_URI
import com.zainal.android.catalogue.helper.MappingHelper
import com.zainal.android.catalogue.model.Movie

internal class StackRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private val favoriteMovie = ArrayList<Movie>()

    override fun onCreate() {}

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        favoriteMovie.clear()
        val movieCursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
        val arrayResults = MappingHelper.mapMovieCursorToArrayList(movieCursor)
        arrayResults.forEach { movie ->
            favoriteMovie.add(movie)
        }
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {}

    override fun getCount(): Int = favoriteMovie.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_favorite_item)
        if (favoriteMovie.isNotEmpty()){
            val imagePoster = Glide.with(context)
                .asBitmap()
                .load(favoriteMovie[position].backdrop)
                .submit()
                .get()
            rv.setImageViewBitmap(R.id.imageView, imagePoster)
        }
        val extras = bundleOf(
            ImageFavoriteWidget.EXTRA_ITEM to favoriteMovie[position].original_name
        )

        val fillIntent = Intent()
        fillIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}