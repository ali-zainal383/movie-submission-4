package com.zainal.android.favoriteapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zainal.android.favoriteapp.R
import com.zainal.android.favoriteapp.detail.DetailFavoriteTvShowActivity
import com.zainal.android.favoriteapp.model.TvShow
import com.zainal.android.catalogue.custom.CustomOnItemClickListener
import kotlinx.android.synthetic.main.item_favorite_tv_show.view.*

class TvShowFavoriteAdapter : RecyclerView.Adapter<TvShowFavoriteAdapter.CardViewHolder>() {

    var listFavoriteTvShows = ArrayList<TvShow>()
        set(listFavoriteTvShows) {
            if (listFavoriteTvShows.size > 0) {
                this.listFavoriteTvShows.clear()
            }
            this.listFavoriteTvShows.addAll(listFavoriteTvShows)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_favorite_tv_show, viewGroup, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listFavoriteTvShows[position])
    }

    override fun getItemCount(): Int = this.listFavoriteTvShows.size

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvShow: TvShow) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(tvShow.poster)
                    .apply(RequestOptions().override(210, 300))
                    .into(iv_fav_tv_poster)

                tv_fav_tv_title.text = tvShow.original_name
                tv_fav_tv_release.text = tvShow.release_date
                tv_fav_tv_overview.text = tvShow.overview

                itemView.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(context, DetailFavoriteTvShowActivity::class.java)
                        intent.putExtra(DetailFavoriteTvShowActivity.EXTRA_TV_SHOW, tvShow)
                        context.startActivity(intent)
                    }
                }))
            }
        }
    }
}