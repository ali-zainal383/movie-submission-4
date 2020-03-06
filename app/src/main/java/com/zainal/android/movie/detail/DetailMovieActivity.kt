package com.zainal.android.movie.detail

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zainal.android.movie.R
import com.zainal.android.movie.db.DatabaseContract.FavoriteMovie.Companion.BACKDROP
import com.zainal.android.movie.db.DatabaseContract.FavoriteMovie.Companion.ORIGINAL_TITLE
import com.zainal.android.movie.db.DatabaseContract.FavoriteMovie.Companion.OVERVIEW
import com.zainal.android.movie.db.DatabaseContract.FavoriteMovie.Companion.POSTER
import com.zainal.android.movie.db.DatabaseContract.FavoriteMovie.Companion.RELEASE_DATE
import com.zainal.android.movie.db.DatabaseContract.FavoriteMovie.Companion._ID
import com.zainal.android.movie.helper.MovieHelper
import com.zainal.android.movie.model.Movie
import com.zainal.android.movie.favorite.movie.TabMovieAdapter
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovieActivity : AppCompatActivity(), View.OnClickListener {

    private var isFavorite = false
    private lateinit var movieHelper: MovieHelper
    private lateinit var tabMovieAdapter: TabMovieAdapter

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_DELETE = 301
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        val actionBarTitle: String
        val btnTitle: String

        tabMovieAdapter = TabMovieAdapter()
        movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()
        Log.d("debug", "onCreate: Open Database")

        val movie = intent.getParcelableExtra(EXTRA_MOVIE) as Movie

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

        if (movieHelper.checkFavorite(movie.id)) {
            actionBarTitle = "Detail Favorite Movie"
            btnTitle = "Delete"
            isFavorite = true
            movieHelper.close()
            Log.d("debug", "true : Close Connection after check movie data")
        } else {
            actionBarTitle = "Detail  Movie"
            btnTitle = "Favorite"
            movieHelper.close()
            Log.d("debug", "false : Close Connection after check movie data")
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bt_favorite_movie.text = btnTitle
        bt_favorite_movie.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
                Log.d("debug", "Delete : Open database before delete")
                val delete = movieHelper.deleteById(movie.id.toString()).toLong()
                if (delete > 0) {
                    setResult(RESULT_DELETE, intent)
                    Toast.makeText(this, "Item was successfully deleted", Toast.LENGTH_SHORT).show()
                    movieHelper.close()
                    Log.d("debug", "Delete : Close database after delete ")
                    finish()
                }
            } else {
                movieHelper.open()
                Log.d("debug", "Insert : Open database before insert ")
                val insert = movieHelper.insert(values)
                if (insert > 0) {
                    setResult(RESULT_ADD, intent)
                    tabMovieAdapter.addItem(movie)
                    Toast.makeText(this, "Item was successfully added", Toast.LENGTH_SHORT).show()
                    movieHelper.close()
                    Log.d("debug", "Insert : Close database after insert")
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        movieHelper.close()
        Log.d("debug", "onDestroy: Close Database")
    }
}

