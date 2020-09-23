package com.zainal.android.catalogue.favorite.movie

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zainal.android.catalogue.R
import com.zainal.android.catalogue.db.DatabaseContract.FavoriteMovie.Companion.CONTENT_URI
import com.zainal.android.catalogue.helper.MovieHelper
import com.zainal.android.catalogue.helper.MappingHelper
import com.zainal.android.catalogue.model.Movie
import kotlinx.android.synthetic.main.fragment_tab_favorite_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class TabFavoriteMovieFragment : Fragment() {

    private lateinit var tabFavoriteMovieAdapter: TabFavoriteMovieAdapter
    private lateinit var movieHelper: MovieHelper

    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_favorite_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_favorite_movie.layoutManager = LinearLayoutManager(context)

        tabFavoriteMovieAdapter = TabFavoriteMovieAdapter()
        tabFavoriteMovieAdapter.notifyDataSetChanged()
        rv_favorite_movie.adapter = tabFavoriteMovieAdapter

        movieHelper = MovieHelper.getInstance(activity!!.applicationContext)
        movieHelper.open()

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

        if (savedInstanceState == null) {
            loadMoviesAsync()
        }else {
            val list = savedInstanceState.getParcelableArrayList<Movie>(EXTRA_STATE)
            if (list != null) {
                tabFavoriteMovieAdapter.listMovies = list
            }
        }
    }

    private fun loadMoviesAsync() {
        GlobalScope.launch(Dispatchers.Main) {

            val deferredMovies = async(Dispatchers.IO) {
                val cursor = activity?.contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapMovieCursorToArrayList(cursor)
            }
            val movies = deferredMovies.await()
            if (movies.size > 0) {
                pb_favorite_movie?.visibility = View.INVISIBLE
                tabFavoriteMovieAdapter.listMovies = movies
                rv_favorite_movie.adapter = tabFavoriteMovieAdapter
            } else {
                pb_favorite_movie?.visibility = View.VISIBLE
                tabFavoriteMovieAdapter.listMovies = ArrayList()
//                Toast.makeText(context, "No data favorite", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
