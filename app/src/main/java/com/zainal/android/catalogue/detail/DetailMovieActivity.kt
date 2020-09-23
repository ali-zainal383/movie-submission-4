package com.zainal.android.catalogue.detail

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zainal.android.catalogue.R
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.BACKDROP
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.CONTENT_URI
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.ORIGINAL_TITLE
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.OVERVIEW
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.POSTER
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.RELEASE_DATE
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion._ID
import com.zainal.android.catalogue.helper.MovieHelper
import com.zainal.android.catalogue.model.Movie
import com.zainal.android.catalogue.settings.SettingsActivity
import com.zainal.android.catalogue.favorite.movie.TabFavoriteMovieAdapter
import com.zainal.android.catalogue.widget.ImageFavoriteWidget
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovieActivity : AppCompatActivity(), View.OnClickListener {

    private var isFavorite = false
    private lateinit var movieHelper: MovieHelper
    private lateinit var tabFavoriteMovieAdapter: TabFavoriteMovieAdapter
    private lateinit var uriWithId: Uri

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_POSITION = "extra_position"
    }

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        val actionBarTitle: String
        val btnTitle: String

        tabFavoriteMovieAdapter =
            TabFavoriteMovieAdapter()

        movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()

        val movie = intent.getParcelableExtra(EXTRA_MOVIE) as Movie
        uriWithId = Uri.parse(CONTENT_URI.toString()+"/"+ movie.id )

        val poster = movie.poster
        val backdrop = movie.backdrop
        val title = movie.original_name
        val release = movie.release_date
        val overview = movie.overview

        tv_movie_title.text = title
        tv_movie_release.text = release
        tv_movie_overview.text = overview

        Glide.with(this)
            .load(backdrop)
            .apply(RequestOptions().override(450,650))
            .into(iv_movie_backdrop)

        Glide.with(this)
            .load(poster)
            .apply(RequestOptions().override(350,450))
            .into(iv_movie_poster)

        val data = contentResolver?.query(uriWithId, null, null, null, null)
        if (data?.moveToFirst()!!) {
            actionBarTitle = "Detail Favorite Movie"
            btnTitle = "Delete"
            isFavorite = true
        } else {
            actionBarTitle = "Detail  Movie"
            btnTitle = "Favorite"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bt_favorite_movie.text = btnTitle
        bt_favorite_movie.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_settings -> {
                val mIntent = Intent(this, SettingsActivity::class.java)
                startActivity(mIntent)
            }
            android.R.id.home -> {
                onBackPressed()
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.bt_favorite_movie) {
            val movie = intent.getParcelableExtra(EXTRA_MOVIE) as Movie
            val id = movie.id
            val title = movie.original_name
            val release = movie.release_date
            val overview = movie.overview
            val poster = movie.poster
            val backdrop = movie.backdrop

            val values = ContentValues()

            values.put(_ID, id)
            values.put(ORIGINAL_TITLE, title)
            values.put(RELEASE_DATE, release)
            values.put(OVERVIEW, overview)
            values.put(POSTER, poster)
            values.put(BACKDROP, backdrop)

            if (isFavorite) {
                movieHelper.open()
                Log.d("Helper Open : ", "Open connection to database")
                contentResolver.delete(uriWithId, null, null)
                val widget = ImageFavoriteWidget
                widget.goUpdateWidget(this)
                Toast.makeText(this, "Item was successfully deleted", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                movieHelper.open()
                Log.d("Helper Open : ", "Open connection to database")
                contentResolver.insert(CONTENT_URI, values)
                val widget = ImageFavoriteWidget
                widget.goUpdateWidget(this)
                Toast.makeText(this, "Item was successfully added", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}

