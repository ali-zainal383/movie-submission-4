package com.zainal.android.movie.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.zainal.android.movie.R
import com.zainal.android.movie.helper.MappingHelper
import com.zainal.android.movie.model.Movie

internal class StackRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private val favoriteMovie = ArrayList<Movie>()

    override fun onCreate() {}

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        favoriteMovie.clear()
//        val movieCursor =
//        val arrayResults = MappingHelper.mapMovieCursorToArrayList(movieCursor)
//        arrayResults.forEach { movie ->
//            favoriteMovie.add(movie)
//        }
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {}

    override fun getCount(): Int = favoriteMovie.size

    override fun getViewAt(position: Int): RemoteViews {
        val movie = favoriteMovie[position]
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        val imagePoster : Bitmap = Glide.with(context)
            .asBitmap()
            .load(movie.poster)
            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .get()

        rv.setImageViewBitmap(R.id.imageView, imagePoster)

        val extras = bundleOf(
            ImageFavoriteWidget.EXTRA_ITEM to movie.original_name
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