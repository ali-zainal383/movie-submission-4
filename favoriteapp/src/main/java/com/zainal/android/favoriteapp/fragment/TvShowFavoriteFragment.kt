package com.zainal.android.favoriteapp.fragment

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.zainal.android.favoriteapp.R
import com.zainal.android.favoriteapp.adapters.TvShowFavoriteAdapter
import com.zainal.android.favoriteapp.database.DatabaseContract.FavoriteTvShow.Companion.CONTENT_URI
import com.zainal.android.favoriteapp.helper.MappingHelper
import kotlinx.android.synthetic.main.fragment_favorite_tv_show.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class TvShowFavoriteFragment : Fragment() {

    private lateinit var tvShowFavoriteAdapter: TvShowFavoriteAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_favorite_tv.layoutManager = LinearLayoutManager(context)

        tvShowFavoriteAdapter = TvShowFavoriteAdapter()
        tvShowFavoriteAdapter.notifyDataSetChanged()
        rv_favorite_tv.adapter = tvShowFavoriteAdapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadTvShowAsync()
            }
        }
        activity?.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)
        loadTvShowAsync()
    }

    private fun loadTvShowAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredTvShows = async(Dispatchers.IO) {
                val cursor = activity?.contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapTvShowCursorToArrayList(cursor)
            }
            val tvShows = deferredTvShows.await()
            if (tvShows.size > 0) {
                pb_favorite_tv?.visibility = View.GONE
                tvShowFavoriteAdapter.listFavoriteTvShows = tvShows
                rv_favorite_tv.adapter = tvShowFavoriteAdapter
            } else {
                pb_favorite_tv?.visibility = View.VISIBLE
                tvShowFavoriteAdapter.listFavoriteTvShows = ArrayList()
            }
        }
    }
}
