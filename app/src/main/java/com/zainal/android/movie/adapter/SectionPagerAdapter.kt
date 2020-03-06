package com.zainal.android.movie.adapter

import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.zainal.android.movie.R
import com.zainal.android.movie.ui.favorite.FavoriteFragment
import com.zainal.android.movie.ui.favorite.movie.TabMovieFragment
import com.zainal.android.movie.ui.favorite.tv.TabTvFragment

class SectionPagerAdapter(private val mContext: FavoriteFragment, fm: FragmentManager) : FragmentStatePagerAdapter (fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_title_movie, R.string.tab_title_tv)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = TabMovieFragment()
            1 -> fragment = TabTvFragment()
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int = 2
}