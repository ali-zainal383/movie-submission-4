package com.zainal.android.catalogue.adapter

import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.zainal.android.catalogue.R
import com.zainal.android.catalogue.favorite.FavoriteFragment
import com.zainal.android.catalogue.favorite.movie.TabFavoriteMovieFragment
import com.zainal.android.catalogue.favorite.tvShow.TabFavoriteTvFragment

class SectionPagerAdapter(private val mContext: FavoriteFragment, fm: FragmentManager) : FragmentStatePagerAdapter (fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val pageTitles = intArrayOf(R.string.tab_title_movie, R.string.tab_title_tv)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment =
                TabFavoriteMovieFragment()
            1 -> fragment =
                TabFavoriteTvFragment()
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(pageTitles[position])
    }

    override fun getCount(): Int = 2
}