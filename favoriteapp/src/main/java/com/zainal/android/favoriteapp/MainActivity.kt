package com.zainal.android.favoriteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.zainal.android.favoriteapp.adapters.SectionsPagerAdapter
import com.zainal.android.favoriteapp.fragment.MovieFavoriteFragment
import com.zainal.android.favoriteapp.fragment.TvShowFavoriteFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

        val movieFavoriteFragment: Fragment = MovieFavoriteFragment()
        val tvShowFavoriteFragment: Fragment = TvShowFavoriteFragment()

        sectionsPagerAdapter.addPage(movieFavoriteFragment)
        sectionsPagerAdapter.addPage(tvShowFavoriteFragment)

        v_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(v_pager)

        supportActionBar?.elevation = 0f
    }
}