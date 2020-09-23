package com.zainal.android.catalogue.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zainal.android.catalogue.R
import com.zainal.android.catalogue.custom.CustomOnItemClickListener
import com.zainal.android.catalogue.model.Movie
import com.zainal.android.catalogue.detail.DetailMovieActivity
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

                itemView.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback{
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(itemView.context, DetailMovieActivity::class.java)
                        intent.putExtra(DetailMovieActivity.EXTRA_POSITION, position)
                        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movie)
                        context.startActivity(intent)
                    }
                }))
            }
        }
    }
}