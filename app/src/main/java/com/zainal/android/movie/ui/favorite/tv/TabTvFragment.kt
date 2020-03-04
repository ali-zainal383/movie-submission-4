package com.zainal.android.movie.ui.favorite.tv

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.zainal.android.movie.R
import com.zainal.android.movie.helper.TvShowHelper
import com.zainal.android.movie.model.TvShow
import com.zainal.android.movie.helper.MappingHelper
import com.zainal.android.movie.detail.DetailTvShowActivity
import kotlinx.android.synthetic.main.fragment_tab_tv.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class TabTvFragment : Fragment() {

    private lateinit var tabTvAdapter: TabTvAdapter
    private lateinit var tvShowHelper: TvShowHelper

    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_tv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_favorite_tv.layoutManager = LinearLayoutManager(context)

//        Adapter
        tabTvAdapter = TabTvAdapter()
        tabTvAdapter.notifyDataSetChanged()
        rv_favorite_tv.adapter = tabTvAdapter

//        Load Helper
        tvShowHelper = TvShowHelper.getInstance(activity!!.applicationContext)
        tvShowHelper.open()


        if (savedInstanceState == null) {
            loadTvShowAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<TvShow>(EXTRA_STATE)
            if (list != null) {
                tabTvAdapter.listTvShows = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, tabTvAdapter.listTvShows)
    }

    private fun loadTvShowAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            pb_favorite_tv.visibility = View.VISIBLE
            val deferredTvShows = async(Dispatchers.IO) {
                val cursor = tvShowHelper.queryAll()
                MappingHelper.mapTvShowCursorToArrayList(cursor)
            }
            val tvShows = deferredTvShows.await()
            if (tvShows.size > 0) {
                pb_favorite_tv.visibility = View.INVISIBLE
                tabTvAdapter.listTvShows = tvShows
                rv_favorite_tv.adapter = tabTvAdapter
            } else {
                pb_favorite_tv.visibility = View.VISIBLE
                tabTvAdapter.listTvShows = ArrayList()
                Toast.makeText(context, "No data favorite", Toast.LENGTH_SHORT).show()
            }
            tvShowHelper.close()
            Log.d("debug", "load TvShow: Close database")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                DetailTvShowActivity.REQUEST_ADD ->
                    if (resultCode == DetailTvShowActivity.RESULT_ADD) {
                        val tvShow = data.getParcelableExtra<TvShow>(DetailTvShowActivity.EXTRA_TVSHOW)
                        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                        tabTvAdapter.addItem(tvShow)
                        rv_favorite_tv.smoothScrollToPosition(tabTvAdapter.itemCount - 1)
                    }
                DetailTvShowActivity.REQUEST_UPDATE ->
                    if (resultCode == DetailTvShowActivity.RESULT_DELETE) {
                        val position = data.getIntExtra(DetailTvShowActivity.EXTRA_POSITION, 0)
                        tabTvAdapter.removeItem(position)
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tvShowHelper.close()
    }
}
