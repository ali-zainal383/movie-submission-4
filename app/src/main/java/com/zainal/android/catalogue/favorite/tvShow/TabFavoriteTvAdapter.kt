package com.zainal.android.catalogue.favorite.tvShow

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zainal.android.catalogue.custom.CustomOnItemClickListener
import com.zainal.android.catalogue.R
import com.zainal.android.catalogue.helper.TvShowHelper
import com.zainal.android.catalogue.model.TvShow
import com.zainal.android.catalogue.detail.DetailTvShowActivity
import kotlinx.android.synthetic.main.item_favorite_tv.view.*

class TabFavoriteTvAdapter : RecyclerView.Adapter<TabFavoriteTvAdapter.CardViewHolder>() {

    private lateinit var tvShowHelper : TvShowHelper

    var listTvShows = ArrayList<TvShow>()
        set(listTvShows) {
            if (listTvShows.size > 0) {
                this.listTvShows.clear()
            }
            this.listTvShows.addAll(listTvShows)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_favorite_tv, viewGroup, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listTvShows[position])
        tvShowHelper = TvShowHelper.getInstance(holder.itemView.context.applicationContext)
        tvShowHelper.open()
    }

    override fun getItemCount(): Int = this.listTvShows.size

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
                        val intent = Intent(context, DetailTvShowActivity::class.java)
                        intent.putExtra(DetailTvShowActivity.EXTRA_POSITION, position)
                        intent.putExtra(DetailTvShowActivity.EXTRA_TV_SHOW, tvShow)
                        context.startActivity(intent)
                    }
                }))
            }
        }
    }
}