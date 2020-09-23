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
import com.zainal.android.favoriteapp.adapters.MovieFavoriteAdapter
import com.zainal.android.favoriteapp.database.DatabaseContract.FavoriteMovie.Companion.CONTENT_URI
import com.zainal.android.favoriteapp.helper.MappingHelper
import kotlinx.android.synthetic.main.fragment_favorite_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class MovieFavoriteFragment : Fragment() {

    private lateinit var movieFavoriteAdapter: MovieFavoriteAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_favorite_movie.layoutManager = LinearLayoutManager(context)

        movieFavoriteAdapter = MovieFavoriteAdapter()
        movieFavoriteAdapter.notifyDataSetChanged()
        rv_favorite_movie.adapter = movieFavoriteAdapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadMoviesAsync()
            }
        }
        activity?.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)
        loadMoviesAsync()

    }

    private fun loadMoviesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredMovies = async(Dispatchers.IO) {
                val cursor = activity?.contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapMovieCursorToArrayList(cursor)
            }
            val movies = deferredMovies.await()
            if (movies.size > 0) {
                pb_favorite_movie?.visibility = View.GONE
                movieFavoriteAdapter.listFavoriteMovies = movies
                rv_favorite_movie.adapter = movieFavoriteAdapter
            } else {
                pb_favorite_movie?.visibility = View.VISIBLE
                movieFavoriteAdapter.listFavoriteMovies = ArrayList()
            }
        }
    }
}
