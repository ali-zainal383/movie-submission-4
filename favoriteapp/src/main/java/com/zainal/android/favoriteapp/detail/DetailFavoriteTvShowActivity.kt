package com.zainal.android.favoriteapp.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zainal.android.favoriteapp.R
import com.zainal.android.favoriteapp.adapters.TvShowFavoriteAdapter
import com.zainal.android.favoriteapp.model.TvShow
import kotlinx.android.synthetic.main.activity_detail_favorite_tv.*

class DetailFavoriteTvShowActivity : AppCompatActivity() {

    private lateinit var tvShowFavoriteAdapter: TvShowFavoriteAdapter

    companion object {
        const val EXTRA_TV_SHOW = "extra_tv_show"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_favorite_tv)

        supportActionBar?.title = "Detail Favorite Tv Show"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvShowFavoriteAdapter = TvShowFavoriteAdapter()

        val tvShow = intent.getParcelableExtra(EXTRA_TV_SHOW) as TvShow

        val poster = tvShow.poster
        val backdrop = tvShow.backdrop
        val title = tvShow.original_name
        val release = tvShow.release_date
        val overview = tvShow.overview

        tv_show_title.text = title
        tv_show_release.text = release
        tv_show_overview.text = overview

        Glide.with(this)
            .load(backdrop)
            .apply(RequestOptions().override(450,650))
            .into(iv_show_backdrop)

        Glide.with(this)
            .load(poster)
            .apply(RequestOptions().override(350, 450))
            .into(iv_show_poster)
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
