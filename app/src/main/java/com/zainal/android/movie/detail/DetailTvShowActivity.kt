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
import com.zainal.android.movie.db.DatabaseContract.FavoriteTvShow.Companion.BACKDROP
import com.zainal.android.movie.db.DatabaseContract.FavoriteTvShow.Companion.ORIGINAL_TITLE
import com.zainal.android.movie.db.DatabaseContract.FavoriteTvShow.Companion.OVERVIEW
import com.zainal.android.movie.db.DatabaseContract.FavoriteTvShow.Companion.POSTER
import com.zainal.android.movie.db.DatabaseContract.FavoriteTvShow.Companion.RELEASE_DATE
import com.zainal.android.movie.db.DatabaseContract.FavoriteTvShow.Companion._ID
import com.zainal.android.movie.helper.TvShowHelper
import com.zainal.android.movie.model.TvShow
import com.zainal.android.movie.favorite.tv.TabTvAdapter
import kotlinx.android.synthetic.main.activity_detail_tv_show.*

class DetailTvShowActivity : AppCompatActivity(), View.OnClickListener {

    private var isFavorite = false
    private lateinit var tvShowHelper: TvShowHelper
    private lateinit var tabTvAdapter: TabTvAdapter

    companion object {
        const val EXTRA_TVSHOW = "extra_tvshow"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_DELETE = 301
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tv_show)

        val actionBarTitle: String
        val btnTitle: String

        tabTvAdapter = TabTvAdapter()
        tvShowHelper = TvShowHelper.getInstance(applicationContext)
        tvShowHelper.open()
        Log.d("debug", "onCreate: Open Database")

        val tvShow = intent.getParcelableExtra(EXTRA_TVSHOW) as TvShow

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

        if (tvShowHelper.checkFavorite(tvShow.id)) {
            actionBarTitle = "Detail Favorite Tv Show"
            btnTitle = "Delete"
            isFavorite = true
            tvShowHelper.close()
            Log.d("debug", "true : Close connection after check data")
        } else {
            actionBarTitle = "Detail Tv Show"
            btnTitle = "Favorite"
            tvShowHelper.close()
            Log.d("debug", "false : Close connection after check data")
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bt_favorite_tv_show.text = btnTitle
        bt_favorite_tv_show.setOnClickListener(this)

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
        if (view?.id == R.id.bt_favorite_tv_show) {
            val tvShow = intent.getParcelableExtra(EXTRA_TVSHOW) as TvShow
            val id = tvShow.id
            val title = tvShow.original_name
            val release = tvShow.release_date
            val overview = tvShow.overview
            val poster = tvShow.poster
            val backdrop = tvShow.backdrop

            val values = ContentValues()
            values.put(_ID, id)
            values.put(ORIGINAL_TITLE, title)
            values.put(RELEASE_DATE, release)
            values.put(OVERVIEW, overview)
            values.put(POSTER, poster)
            values.put(BACKDROP, backdrop)

            if (isFavorite) {
                tvShowHelper.open()
                Log.d("debug", "Delete data : Open database before delete")
                val delete = tvShowHelper.deleteById(tvShow.id.toString()).toLong()
                if (delete > 0) {
                    setResult(RESULT_DELETE, intent)
                    Toast.makeText(this, "Item was successfully deleted", Toast.LENGTH_SHORT).show()
                    tvShowHelper.close()
                    Log.d("debug", "Delete data : Close database after delete")
                    finish()
                }
            } else {
                tvShowHelper.open()
                Log.d("debug", "Insert data : Open database before insert")
                val insert = tvShowHelper.insert(values)
                if (insert > 0) {
                    setResult(RESULT_ADD, intent)
                    tabTvAdapter.addItem(tvShow)
                    Toast.makeText(this, "One item was successfully added", Toast.LENGTH_SHORT).show()
                    tvShowHelper.close()
                    Log.d("debug", "Insert data : Close database after delete")
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tvShowHelper.close()
    }
}
