package com.example.chrono.main

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.chrono.R
import com.example.chrono.databinding.ActivityMainBinding
import com.example.chrono.util.BaseActivity
import com.example.chrono.util.helpers.NonSwipeableViewPager

class MainActivity : BaseActivity() {
    private var timerFrag: Fragment = TimerFrag()
    private var swatchFrag: Fragment = StopwatchFrag()

    var pager: NonSwipeableViewPager? = null
    var bind: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        val adapter = MainAdapter(supportFragmentManager)
        pager = bind!!.pager

        pager!!.adapter = adapter
        pager!!.offscreenPageLimit = 2

        bind!!.navtoggle.setOnClickListener {
            if (bind!!.navtoggle.isChecked) {

            } else {

            }
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
}
