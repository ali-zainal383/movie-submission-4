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
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.BACKDROP
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.CONTENT_URI
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.ORIGINAL_TITLE
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.OVERVIEW
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.POSTER
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.RELEASE_DATE
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion._ID
import com.zainal.android.catalogue.helper.TvShowHelper
import com.zainal.android.catalogue.model.TvShow
import com.zainal.android.catalogue.settings.SettingsActivity
import com.zainal.android.catalogue.favorite.tvShow.TabFavoriteTvAdapter
import kotlinx.android.synthetic.main.activity_detail_tv_show.*

class DetailTvShowActivity : AppCompatActivity(), View.OnClickListener {

    private var isFavorite = false
    private lateinit var tvShowHelper: TvShowHelper
    private lateinit var tabFavoriteTvAdapter: TabFavoriteTvAdapter
    private lateinit var uriWithId: Uri

    companion object {
        const val EXTRA_TV_SHOW = "extra_tv_show"
        const val EXTRA_POSITION = "extra_position"
    }

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tv_show)

        val actionBarTitle: String
        val btnTitle: String

        tabFavoriteTvAdapter =
            TabFavoriteTvAdapter()

        tvShowHelper = TvShowHelper.getInstance(applicationContext)
        tvShowHelper.open()

        val tvShow = intent.getParcelableExtra(EXTRA_TV_SHOW) as TvShow
        uriWithId = Uri.parse(CONTENT_URI.toString()+"/"+ tvShow.id)

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

        val data = contentResolver?.query(uriWithId, null, null, null, null)
        if (data?.moveToFirst()!!) {
            actionBarTitle = "Detail Favorite Tv Show"
            btnTitle = "Delete"
            isFavorite = true
        } else {
            actionBarTitle = "Detail Tv Show"
            btnTitle = "Favorite"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bt_favorite_tv_show.text = btnTitle
        bt_favorite_tv_show.setOnClickListener(this)

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
        if (view?.id == R.id.bt_favorite_tv_show) {
            val tvShow = intent.getParcelableExtra(EXTRA_TV_SHOW) as TvShow
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
                Log.d("Helper Open: ", "Open connection to database")
                contentResolver.delete(uriWithId, null, null)
                Toast.makeText(this, "Item was successfully deleted", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                tvShowHelper.open()
                Log.d("Helper Open: ", "Open connection to database")
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, "One item was successfully added", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
