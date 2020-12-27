package com.example.chrono.main

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.chrono.R
import com.example.chrono.databinding.ActivityMainBinding
import com.example.chrono.main.stopwatch.StopwatchFrag
import com.example.chrono.main.timer.CircuitFrag
import com.example.chrono.main.timer.CircuitDashboardFrag
import com.example.chrono.util.BaseActivity
import com.example.chrono.util.PreferenceManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseActivity() {

    private lateinit var pager: ViewPager2 // ViewPager where the fragments sit
    private var bind: ActivityMainBinding? = null // Bind variable for the activity
    private lateinit var tablay: TabLayout // The timer/stopwatch navigation tab layout

    private lateinit var circuitFrag: CircuitFrag
    private lateinit var circuitDashFrag: CircuitDashboardFrag
    private lateinit var stopwatchFrag: StopwatchFrag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        PreferenceManager.with(this)

        // Initialize Frags
        circuitFrag = CircuitFrag()
        circuitDashFrag = CircuitDashboardFrag()
        stopwatchFrag = StopwatchFrag()

        // Set the pager and tab layouts by finding them in the bound layout
        pager = bind!!.pager
        tablay = bind!!.tablayout

        // Set the adapter of the viewpager (our custom fragment adapter below)
        val adapter = TabsPagerAdapter(supportFragmentManager, lifecycle)
        pager.adapter = adapter

        // Tells the tablayout to follow the viewpager
        TabLayoutMediator(tablay, pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Timer"
                else -> tab.text = "Stopwatch"
            }
        }.attach()

        // Change up some UI elements based on light/dark mode
        if (isUsingNightModeResources()) {
            tablay.background = ContextCompat.getDrawable(this, R.drawable.nav_tab_dark)
        } else {
            tablay.background = ContextCompat.getDrawable(this, R.drawable.nav_tab_light)
        }
    }

    // Our custom tab fragment adapter
    private inner class TabsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fm, lifecycle) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    circuitDashFrag
                }
                else -> {
                    stopwatchFrag
                }
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }
}


