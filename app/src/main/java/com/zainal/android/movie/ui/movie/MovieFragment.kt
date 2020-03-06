package com.zainal.android.movie.ui.movie

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zainal.android.movie.R
import com.zainal.android.movie.ui.detail.DetailMovieActivity
import com.zainal.android.movie.model.Movie
import com.zainal.android.movie.search.SearchResultActivity
import com.zainal.android.movie.settings.SettingsActivity
import com.zainal.android.movie.viewmodels.MovieViewModel
import com.zainal.android.movie.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var searchViewModel: SearchViewModel

    companion object {
        const val type: String = "Movie"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        showMovie()
    }

    private fun showMovie() {
        rv_movie.layoutManager = LinearLayoutManager(context)
        val movieAdapter = MovieAdapter()
        movieAdapter.notifyDataSetChanged()
        rv_movie.adapter = movieAdapter

        movieViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MovieViewModel::class.java)
        movieViewModel.setMovie()

        movieViewModel.getMovie().observe(viewLifecycleOwner, Observer { movieItems ->
            if (movieItems != null) {
                movieAdapter.setData(movieItems)
            }
            showLoading(false)
        })

        movieAdapter.setItemClickCallback(object : MovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Movie) {
                val detailMovie = Intent(activity, DetailMovieActivity::class.java)
                detailMovie.putExtra(DetailMovieActivity.EXTRA_MOVIE, data)
                startActivity(detailMovie)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        searchViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SearchViewModel::class.java)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }
        searchView.setIconifiedByDefault(false)
        searchView.queryHint = resources.getString(R.string.search_movie)
        searchView.requestFocusFromTouch()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchMovieIntent = Intent(activity, SearchResultActivity::class.java)
                searchMovieIntent.putExtra(SearchResultActivity.EXTRA_QUERY, query)
                searchMovieIntent.putExtra(SearchResultActivity.EXTRA_TYPE, type)
                if (query != null) {
                    searchViewModel.setSearchResult(query, type)
                }
                startActivity(searchMovieIntent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_change_settings -> {
                val settingsIntent = Intent(context, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pb_movie.visibility = View.VISIBLE
        } else {
            pb_movie.visibility = View.GONE
        }
    }
}


