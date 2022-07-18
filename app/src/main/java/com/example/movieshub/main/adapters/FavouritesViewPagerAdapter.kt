package com.example.movieshub.main.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.movieshub.main.fragments.favourites_tabs.MovieFragment
import com.example.movieshub.main.fragments.favourites_tabs.TvFragment

class FavouritesViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        if (position == 0) {
            fragment = MovieFragment.newInstance()
        }
        else if (position == 1) {
            fragment = TvFragment.newInstance()
        }

        return fragment
    }

    override fun getCount(): Int {
        return  2
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = "Movies"
        } else if (position == 1) {
            title = "TV Shows"
        }
        return title
    }


}