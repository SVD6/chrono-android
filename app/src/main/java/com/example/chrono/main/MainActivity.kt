package com.example.chrono.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.chrono.R
import com.example.chrono.databinding.ActivityMainBinding
import com.example.chrono.util.BaseActivity
import com.example.chrono.util.helpers.NonSwipeableViewPager
import com.example.chrono.util.helpers.TabFragmentAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var timerFrag: TimerFrag
    private lateinit var swatchFrag: StopwatchFrag

    var pager: ViewPager? = null
    var bind: ActivityMainBinding? = null
    var indicator: View? = null
    var tablay: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)

        pager = bind!!.pager
        indicator = bind!!.indicator
        tablay = bind!!.tablayout

        val fragmentAdapter = TabFragmentAdapter(supportFragmentManager)
        pager!!.adapter = fragmentAdapter

        tablay!!.setupWithViewPager(pager)

        if (isUsingNightModeResources()) {
            tablay!!.background = ContextCompat.getDrawable(this, R.drawable.tab_dark)
        } else {
            tablay!!.background = ContextCompat.getDrawable(this, R.drawable.tab_light)
        }

    }

    private inner class MainAdapter internal constructor(fm: FragmentManager) :
        FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> timerFrag
                else -> swatchFrag
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}
