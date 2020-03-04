package com.zainal.android.movie.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zainal.android.movie.R
import com.zainal.android.movie.model.TvShow
import kotlinx.android.synthetic.main.item_search_result.view.*

class TvShowSearchAdapter : RecyclerView.Adapter<TvShowSearchAdapter.CardViewHolder>() {

    private val tvShowResult = ArrayList<TvShow>()

    fun setData(items: ArrayList<TvShow>){
        tvShowResult.clear()
        tvShowResult.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_search_result, viewGroup, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(tvShowResult[position])
    }

    override fun getItemCount(): Int = tvShowResult.size

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvShow: TvShow) {
            with(itemView){
                Glide.with(itemView.context)
                    .load(tvShow.poster)
                    .apply(RequestOptions().override(210,330))
                    .into(iv_search_poster)

                tv_search_title.text = tvShow.original_name
                tv_search_release.text = tvShow.release_date
                tv_search_overview.text = tvShow.overview
            }
        }

    }
}