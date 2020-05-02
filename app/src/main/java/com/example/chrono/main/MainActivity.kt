package com.example.chrono.main

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.chrono.R
import com.example.chrono.databinding.ActivityMainBinding
import com.example.chrono.util.BaseActivity
import com.google.android.material.tabs.TabLayout


class MainActivity : BaseActivity() {

    var pager: ViewPager? = null //
    var bind: ActivityMainBinding? = null
    private var tablay: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)

        pager = bind!!.pager
        tablay = bind!!.tablayout

        val fragmentAdapter = TabFragmentAdapter(supportFragmentManager)
        pager!!.adapter = fragmentAdapter

        tablay!!.setupWithViewPager(pager)

        if (isUsingNightModeResources()) {
            tablay!!.background = ContextCompat.getDrawable(this, R.drawable.nav_tab_dark)
        } else {
            tablay!!.background = ContextCompat.getDrawable(this, R.drawable.nav_tab_light)
        }
    }

    private inner class TabFragmentAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    TimerFrag()
                }
                else -> {
                    StopwatchFrag()
                }
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "Timer"
                else -> {
                    return "Stopwatch"
                }
            }
        }
    }
}


