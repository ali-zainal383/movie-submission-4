package com.zainal.android.catalogue.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.zainal.android.catalogue.BuildConfig
import com.zainal.android.catalogue.model.TvShow
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class TvShowViewModel : ViewModel() {

    companion object {
        private const val API_KEY = BuildConfig.MovieDB_ApiKey
    }
    val listTv = MutableLiveData<ArrayList<TvShow>>()

    internal fun setTv() {
        val client = AsyncHttpClient()
        val listItems = ArrayList<TvShow>()
        val url = "https://api.themoviedb.org/3/discover/tv?api_key=$API_KEY&language=en-US"
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val tv = list.getJSONObject(i)
                        val tvItems = TvShow()
                        tvItems.id = tv.getInt("id")
                        tvItems.original_name = tv.getString("name")
                        tvItems.release_date = tv.getString("first_air_date")
                        tvItems.overview = tv.getString("overview")
                        val poster = tv.getString("poster_path")
                        tvItems.poster = "https://image.tmdb.org/t/p/w154$poster"
                        val backdrop = tv.getString("backdrop_path")
                        tvItems.backdrop = "https://image.tmdb.org/t/p/w342$backdrop"
                        listItems.add(tvItems)
                    }
                    listTv.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    internal fun getTv() : LiveData<ArrayList<TvShow>> {
        return listTv
    }
}