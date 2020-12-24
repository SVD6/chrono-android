package com.example.chrono.main.timer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        bind!!.createTimer.setOnClickListener {
            startActivity(Intent(requireContext(), TimerCreate::class.java))
        }

        return bind!!.root
    }
}