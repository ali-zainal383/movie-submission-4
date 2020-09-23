package com.zainal.android.catalogue.favorite

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.zainal.android.catalogue.R
import com.zainal.android.catalogue.adapter.SectionPagerAdapter
import com.zainal.android.catalogue.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionPagerAdapter = SectionPagerAdapter(this, childFragmentManager)
        v_pager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(v_pager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_change_settings -> {
                val settingsIntent = Intent(context, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }
}