package com.example.chrono.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.chrono.R
import com.example.chrono.databinding.FragmentTimerBinding

class TimerFrag : Fragment() {
    var bind: FragmentTimerBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false)
        return bind!!.root

        // Change up some UI elements based on light/dark mode
        if ((activity as MainActivity).isUsingNightModeResources()) {
            bind!!.progressbar.background =
                ContextCompat.getDrawable(context!!, R.drawable.circle_dark)
        } else {
            bind!!.progressbar.background =
                ContextCompat.getDrawable(context!!, R.drawable.circle_light)
        }
    }

}