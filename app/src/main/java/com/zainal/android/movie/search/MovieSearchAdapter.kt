package com.zainal.android.movie.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zainal.android.movie.R
import com.zainal.android.movie.model.Movie
import kotlinx.android.synthetic.main.item_search_result.view.*

class MovieSearchAdapter: RecyclerView.Adapter<MovieSearchAdapter.CardViewHolder>()  {

    private val movieResult = ArrayList<Movie>()

    fun setData(items: ArrayList<Movie>) {
        movieResult.clear()
        movieResult.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_search_result, viewGroup, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
       holder.bind(movieResult[position])
    }

    override fun getItemCount(): Int = movieResult.size

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(movie: Movie) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(movie.poster)
                    .apply(RequestOptions().override(210, 330))
                    .into(iv_search_poster)

                tv_search_title.text = movie.original_name
                tv_search_release.text = movie.release_date
                tv_search_overview.text = movie.overview
            }
        }

    }
}