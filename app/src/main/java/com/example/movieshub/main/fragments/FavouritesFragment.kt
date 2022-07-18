package com.example.movieshub.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager

import com.example.movieshub.R
import com.example.movieshub.databinding.FragmentFavouritesBinding
import com.example.movieshub.databinding.FragmentTvBinding
import com.example.movieshub.main.adapters.HomePagerAdapter
import com.example.movieshub.main.fragments.favourites_tabs.MovieFragment
import com.example.movieshub.main.fragments.favourites_tabs.TvFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class FavouritesFragment : Fragment() {
    private lateinit var layout: FragmentFavouritesBinding

    lateinit var viewPager:ViewPager
    lateinit var tabLayout:TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        layout = FragmentFavouritesBinding.inflate(inflater, container, false)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager()

        layout.favNavigationMenu.setOnNavigationItemSelectedListener(onFavNavigationItemSelectedListener)

        layout.favNavigationMenu.setOnNavigationItemReselectedListener {
            return@setOnNavigationItemReselectedListener
        }

    }

    private fun setUpViewPager(){
        val adapter = HomePagerAdapter(childFragmentManager)
        adapter.addFrag(MovieFragment.newInstance(), "Movies")
        adapter.addFrag(TvFragment.newInstance(), "TV Shows")

        layout.pager.adapter = adapter

        layout.pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        layout.favNavigationMenu.selectedItemId = R.id.subTabMovie
                    }
                    1 -> {
                        layout.favNavigationMenu.selectedItemId = R.id.subTabTV
                    }
                }
            }
        })

        layout.pager.currentItem = 0

    }

    private val onFavNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->


        when (item.itemId) {
            R.id.subTabMovie -> {
//                toolbarTitle.text = getString(R.string.titleNotification)
//                BottomMenuHelper.removeBadge(bottomNavigation, R.id.navigationNotification)
                layout.pager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.subTabTV -> {
//                toolbarTitle.text = getString(R.string.titleProjects)
                layout.pager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    companion object {

        fun newInstance(): FavouritesFragment =
            FavouritesFragment()
    }


}
