package com.zainal.android.catalogue.favorite.tvShow

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zainal.android.catalogue.R
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteTvShow.Companion.CONTENT_URI
import com.zainal.android.catalogue.helper.TvShowHelper
import com.zainal.android.catalogue.helper.MappingHelper
import com.zainal.android.catalogue.model.TvShow
import kotlinx.android.synthetic.main.fragment_tab_favorite_tv.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class TabFavoriteTvFragment : Fragment() {

    private lateinit var tabFavoriteTvAdapter: TabFavoriteTvAdapter
    private lateinit var tvShowHelper: TvShowHelper

    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_favorite_tv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_favorite_tv.layoutManager = LinearLayoutManager(context)

        tabFavoriteTvAdapter = TabFavoriteTvAdapter()
        tabFavoriteTvAdapter.notifyDataSetChanged()
        rv_favorite_tv.adapter = tabFavoriteTvAdapter

        tvShowHelper = TvShowHelper.getInstance(activity!!.applicationContext)
        tvShowHelper.open()

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

        if (savedInstanceState == null) {
            loadTvShowAsync()
        }else{
            val list = savedInstanceState.getParcelableArrayList<TvShow>(EXTRA_STATE)
            if (list != null) {
                tabFavoriteTvAdapter.listTvShows = list
            }
        }
    }

    private fun loadTvShowAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredTvShows = async(Dispatchers.IO) {
                val cursor = activity?.contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapTvShowCursorToArrayList(cursor)
            }
            val tvShows = deferredTvShows.await()
            if (tvShows.size > 0) {
                pb_favorite_tv?.visibility = View.INVISIBLE
                tabFavoriteTvAdapter.listTvShows = tvShows
                rv_favorite_tv.adapter = tabFavoriteTvAdapter
            } else {
                pb_favorite_tv?.visibility = View.VISIBLE
                tabFavoriteTvAdapter.listTvShows = ArrayList()
            }
        }
    }
}
