package com.zainal.android.movie.favorite.movie

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zainal.android.movie.custom.CustomOnItemClickListener
import com.zainal.android.movie.R
import com.zainal.android.movie.helper.MovieHelper
import com.zainal.android.movie.detail.DetailMovieActivity
import com.zainal.android.movie.model.Movie
import kotlinx.android.synthetic.main.item_favorite_movie.view.*

class TabMovieAdapter : RecyclerView.Adapter<TabMovieAdapter.CardViewHolder>() {

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

    fun addItem(movie: Movie) {
        this.listMovies.add(movie)
        notifyItemInserted(this.listMovies.size - 1)
    }

    fun removeItem(position: Int) {
        this.listMovies.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listMovies.size)
    }
}