package com.zainal.android.movie.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zainal.android.movie.R
import com.zainal.android.movie.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.activity_search_result.*

class SearchResultActivity : AppCompatActivity() {
    private lateinit var searchViewModel: SearchViewModel

    companion object {
        const val EXTRA_QUERY = "extra_query"
        const val EXTRA_TYPE = "extra_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        supportActionBar?.title = resources.getString(R.string.title_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showLoading(true)
        showSearchResult()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun showSearchResult() {
        val query = intent.getStringExtra(EXTRA_QUERY)
        when (val searchType = intent.getStringExtra(EXTRA_TYPE)) {
            "Movie" -> {
                rv_search_result.layoutManager = LinearLayoutManager(this)
                val movieSearchAdapter = MovieSearchAdapter()
                rv_search_result.adapter = movieSearchAdapter

                searchViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SearchViewModel::class.java)
                searchViewModel.setSearchResult(query, searchType)

                searchViewModel.getSearchResultMovies().observe(this, Observer { movieItems ->
                    if (movieItems != null) {
                        movieSearchAdapter.setData(movieItems)
                    }
                    showLoading(false)
                })
            }
            "Tv Show" -> {
                rv_search_result.layoutManager = LinearLayoutManager(this)
                val tvShowSearchAdapter = TvShowSearchAdapter()
                rv_search_result.adapter = tvShowSearchAdapter

                searchViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SearchViewModel::class.java)
                searchViewModel.setSearchResult(query, searchType)

                searchViewModel.getSearchResultTvShow().observe(this, Observer { tvShowItems ->
                    if (tvShowItems != null) {
                        tvShowSearchAdapter.setData(tvShowItems)
                    }
                    showLoading(false)
                })
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pb_result_movie.visibility = View.VISIBLE
        } else {
            pb_result_movie.visibility = View.GONE
        }
    }
}
