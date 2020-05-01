package com.example.chrono.util.helpers

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.chrono.main.StopwatchFrag
import com.example.chrono.main.TimerFrag

class TabFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

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