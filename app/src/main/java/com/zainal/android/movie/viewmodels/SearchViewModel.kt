package com.zainal.android.movie.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.zainal.android.movie.BuildConfig
import com.zainal.android.movie.model.Movie
import com.zainal.android.movie.model.TvShow
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class SearchViewModel : ViewModel() {

    companion object {
        private const val API_KEY = BuildConfig.MovieDB_ApiKey
    }

    val listResultMovies = MutableLiveData<ArrayList<Movie>>()
    val listResultTvShows = MutableLiveData<ArrayList<TvShow>>()

    internal fun setSearchResult(query: String, type:String) {
        val client = AsyncHttpClient()
        val listResultsMovie = ArrayList<Movie>()
        val listResultsTvShow = ArrayList<TvShow>()
        when (type) {
            "Movie" -> {
                val url = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=en-US&query=$query"
                client.get(url, object : AsyncHttpResponseHandler(){
                    override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                        try {
                            val result = String(responseBody)
                            val responseObject = JSONObject(result)
                            val listResultsItems = responseObject.getJSONArray("results")
                            for (i in 0 until listResultsItems.length()) {
                                val movie = listResultsItems.getJSONObject(i)
                                val movieItems = Movie()
                                movieItems.id = movie.getInt("id")
                                movieItems.original_name = movie.getString("title")
                                movieItems.release_date = movie.getString("release_date")
                                movieItems.overview = movie.getString("overview")
                                val poster = movie.getString("poster_path")
                                movieItems.poster = "https://image.tmdb.org/t/p/w154$poster"
                                val backdrop = movie.getString("backdrop_path")
                                movieItems.backdrop = "https://image.tmdb.org/t/p/w342$backdrop"
                                listResultsMovie.add(movieItems)
                            }
                            listResultMovies.postValue(listResultsMovie)
                        } catch (e: Exception) {
                            Log.d("Exception", e.message.toString())
                        }
                    }
                    override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                        Log.d("onFailure", error?.message.toString())
                    }
                })
            }
            "Tv Show" -> {
                val url = "https://api.themoviedb.org/3/search/tv?api_key=$API_KEY&language=en-US&query=$query"
                client.get(url, object : AsyncHttpResponseHandler(){
                    override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                        try {
                            val result = String(responseBody)
                            val responseObject = JSONObject(result)
                            val listResultsItems = responseObject.getJSONArray("results")
                            for (i in 0 until listResultsItems.length()) {
                                val tvShow = listResultsItems.getJSONObject(i)
                                val tvShowItems = TvShow()
                                tvShowItems.id = tvShow.getInt("id")
                                tvShowItems.original_name = tvShow.getString("name")
                                tvShowItems.release_date = tvShow.getString("first_air_date")
                                tvShowItems.overview = tvShow.getString("overview")
                                val poster = tvShow.getString("poster_path")
                                tvShowItems.poster = "https://image.tmdb.org/t/p/w154$poster"
                                val backdrop = tvShow.getString("backdrop_path")
                                tvShowItems.backdrop = "https://image.tmdb.org/t/p/w342$backdrop"
                                listResultsTvShow.add(tvShowItems)
                            }
                            listResultTvShows.postValue(listResultsTvShow)
                        } catch (e: Exception) {
                            Log.d("Exception", e.message.toString())
                        }
                    }

                    override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?,error: Throwable?) {
                        Log.d("onFailure", error?.message.toString())
                    }
                })
            }
        }
    }

    internal fun getSearchResultMovies(): LiveData<ArrayList<Movie>> {
        return listResultMovies
    }

    internal fun getSearchResultTvShow() : LiveData<ArrayList<TvShow>> {
        return listResultTvShows
    }
}