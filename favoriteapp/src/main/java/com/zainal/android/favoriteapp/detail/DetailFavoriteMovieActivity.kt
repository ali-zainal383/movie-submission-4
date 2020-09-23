package com.zainal.android.favoriteapp.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zainal.android.favoriteapp.R
import com.zainal.android.favoriteapp.adapters.MovieFavoriteAdapter
import com.zainal.android.favoriteapp.model.Movie
import kotlinx.android.synthetic.main.activity_detail_favorite_movie.*

class DetailFavoriteMovieActivity : AppCompatActivity() {

    private lateinit var movieFavoriteAdapter: MovieFavoriteAdapter

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_favorite_movie)

        supportActionBar?.title = "Detail Favorite Movie"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        movieFavoriteAdapter = MovieFavoriteAdapter()

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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }
}
