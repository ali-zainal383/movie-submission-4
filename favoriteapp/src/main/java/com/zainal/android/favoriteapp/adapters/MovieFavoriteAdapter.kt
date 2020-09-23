package com.zainal.android.favoriteapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zainal.android.favoriteapp.R
import com.zainal.android.favoriteapp.detail.DetailFavoriteMovieActivity
import com.zainal.android.favoriteapp.model.Movie
import com.zainal.android.catalogue.custom.CustomOnItemClickListener
import kotlinx.android.synthetic.main.item_favorite_movie.view.*

class MovieFavoriteAdapter : RecyclerView.Adapter<MovieFavoriteAdapter.CardViewHolder>() {

    var listFavoriteMovies = ArrayList<Movie>()
        set(listMovies) {
            if (listMovies.size > 0) {
                this.listFavoriteMovies.clear()
            }
            this.listFavoriteMovies.addAll(listMovies)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_favorite_movie, viewGroup, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listFavoriteMovies[position])
    }

    override fun getItemCount(): Int = this.listFavoriteMovies.size

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
                        val intent = Intent(itemView.context, DetailFavoriteMovieActivity::class.java)
                        intent.putExtra(DetailFavoriteMovieActivity.EXTRA_MOVIE, movie)
                        context.startActivity(intent)
                    }
                }))
            }
        }
    }
}