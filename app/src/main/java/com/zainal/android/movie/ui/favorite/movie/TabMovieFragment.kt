package com.zainal.android.movie.ui.favorite.movie

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
import com.zainal.android.movie.helper.MovieHelper
import com.zainal.android.movie.helper.MappingHelper
import com.zainal.android.movie.detail.DetailMovieActivity
import com.zainal.android.movie.model.Movie
import kotlinx.android.synthetic.main.fragment_tab_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class TabMovieFragment : Fragment() {

    private lateinit var tabMovieAdapter: TabMovieAdapter
    private lateinit var movieHelper: MovieHelper

    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_favorite_movie.layoutManager = LinearLayoutManager(context)
//        Adapter
        tabMovieAdapter = TabMovieAdapter()
        tabMovieAdapter.notifyDataSetChanged()
        rv_favorite_movie.adapter = tabMovieAdapter

//      load helper
        movieHelper = MovieHelper.getInstance(activity!!.applicationContext)
        movieHelper.open()

        if (savedInstanceState == null) {
            loadMoviesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Movie>(EXTRA_STATE)
            if (list != null) {
                tabMovieAdapter.listMovies = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, tabMovieAdapter.listMovies)
    }

    private fun loadMoviesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            pb_favorite_movie.visibility = View.VISIBLE
            val deferredMovies = async(Dispatchers.IO) {
                val cursor = movieHelper.queryAll()
                MappingHelper.mapMovieCursorToArrayList(cursor)
            }
            pb_favorite_movie.visibility = View.INVISIBLE
            val movies = deferredMovies.await()
            if (movies.size > 0) {
                pb_favorite_movie.visibility = View.INVISIBLE
                tabMovieAdapter.listMovies = movies
                rv_favorite_movie.adapter = tabMovieAdapter
            } else {
                pb_favorite_movie.visibility = View.VISIBLE
                tabMovieAdapter.listMovies = ArrayList()
                Toast.makeText(context, "No data favorite", Toast.LENGTH_SHORT).show()
            }
            movieHelper.close()
            Log.d("debug", "loadMovie: Close database")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                DetailMovieActivity.REQUEST_ADD ->
                    if (resultCode == DetailMovieActivity.RESULT_ADD) {
                        val movie = data.getParcelableExtra<Movie>(DetailMovieActivity.EXTRA_MOVIE)
                        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                        tabMovieAdapter.addItem(movie)
                        rv_favorite_movie.smoothScrollToPosition(tabMovieAdapter.itemCount - 1)
                    }
                DetailMovieActivity.REQUEST_UPDATE ->
                    if (resultCode == DetailMovieActivity.RESULT_DELETE) {
                        val position = data.getIntExtra(DetailMovieActivity.EXTRA_POSITION, 0)
                        tabMovieAdapter.removeItem(position)
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        movieHelper.close()
    }
}
