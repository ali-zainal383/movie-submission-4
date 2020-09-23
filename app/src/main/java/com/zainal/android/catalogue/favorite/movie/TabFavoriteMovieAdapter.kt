package com.zainal.android.catalogue.favorite.movie

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zainal.android.catalogue.custom.CustomOnItemClickListener
import com.zainal.android.catalogue.R
import com.zainal.android.catalogue.helper.MovieHelper
import com.zainal.android.catalogue.detail.DetailMovieActivity
import com.zainal.android.catalogue.model.Movie
import kotlinx.android.synthetic.main.item_favorite_movie.view.*

class TabFavoriteMovieAdapter : RecyclerView.Adapter<TabFavoriteMovieAdapter.CardViewHolder>() {

    private lateinit var movieHelper : MovieHelper

    var listMovies = ArrayList<Movie>()
        set(listMovies) {
            if (listMovies.size > 0) {
                this.listMovies.clear()
            }
            this.listMovies.addAll(listMovies)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_favorite_movie, viewGroup, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listMovies[position])
        movieHelper = MovieHelper.getInstance(holder.itemView.context.applicationContext)
        movieHelper.open()

    }

    override fun getItemCount(): Int = this.listMovies.size

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(movie.poster)
                    .apply(RequestOptions().override(350,450))
                    .into(iv_fav_movie_poster)

                tv_fav_movie_title.text = movie.original_name
                tv_fav_movie_release.text = movie.release_date
                tv_fav_movie_overview.text = movie.overview

                itemView.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
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