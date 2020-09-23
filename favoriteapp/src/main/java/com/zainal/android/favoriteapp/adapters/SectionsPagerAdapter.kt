package com.zainal.android.favoriteapp.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zainal.android.favoriteapp.R

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val listPage = ArrayList<Fragment>()

    private val pageTitles = intArrayOf(R.string.tab_movie, R.string.tab_tv_show)

    override fun getItem(position: Int): Fragment {
        return listPage[position]
    }

    override fun getCount(): Int = listPage.size

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(pageTitles[position])
    }

    fun addPage(fragment: Fragment) {
        listPage.add(fragment)
    }
}