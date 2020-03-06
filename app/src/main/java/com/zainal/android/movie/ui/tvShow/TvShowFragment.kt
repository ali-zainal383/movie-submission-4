package com.zainal.android.movie.ui.tvShow

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
import com.zainal.android.movie.ui.detail.DetailTvShowActivity
import com.zainal.android.movie.model.TvShow
import com.zainal.android.movie.search.SearchResultActivity
import com.zainal.android.movie.settings.SettingsActivity
import com.zainal.android.movie.viewmodels.SearchViewModel
import com.zainal.android.movie.viewmodels.TvShowViewModel
import kotlinx.android.synthetic.main.fragment_tv_show.*

class TvShowFragment : Fragment() {

    private lateinit var tvShowViewModel: TvShowViewModel
    private lateinit var searchViewModel: SearchViewModel

    companion object {
        const val type: String = "Tv Show"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        showTv()
    }

    private fun showTv() {
        rv_tv_show.layoutManager = LinearLayoutManager(context)
        val tvShowAdapter = TvShowAdapter()
        tvShowAdapter.notifyDataSetChanged()
        rv_tv_show.adapter = tvShowAdapter

        tvShowViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(TvShowViewModel::class.java)
        tvShowViewModel.setTv()

        tvShowViewModel.getTv().observe(viewLifecycleOwner, Observer { tvItems ->
            if (tvItems != null) {
                tvShowAdapter.setData(tvItems)
            }
            showLoading(false)
        })

        tvShowAdapter.setOnItemClickCallback(object : TvShowAdapter.OnItemClickCallback {
            override fun onItemClicked(data: TvShow) {
                val detailTvShow = Intent(activity, DetailTvShowActivity::class.java)
                detailTvShow.putExtra(DetailTvShowActivity.EXTRA_TVSHOW, data)
                startActivity(detailTvShow)
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

        val searchView =  (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }
        searchView.setIconifiedByDefault(false)
        searchView.queryHint = resources.getString(R.string.search_tvShow)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchTvShowIntent = Intent(activity, SearchResultActivity::class.java)
                searchTvShowIntent.putExtra(SearchResultActivity.EXTRA_QUERY, query)
                searchTvShowIntent.putExtra(SearchResultActivity.EXTRA_TYPE, type)
                if (query != null) {
                    searchViewModel.setSearchResult(query, type)
                }
                startActivity(searchTvShowIntent)
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
            pb_tv.visibility = View.VISIBLE
        } else {
            pb_tv.visibility = View.GONE
        }
    }
}