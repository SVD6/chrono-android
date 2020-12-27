package com.example.chrono.main.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.chrono.R
import com.example.chrono.databinding.FragmentCircuitDashboardBinding

class CircuitDashboardFrag : Fragment() {
    private var bind: FragmentCircuitDashboardBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind =
            DataBindingUtil.inflate(inflater, R.layout.fragment_circuit_dashboard, container, false)

        return bind!!.root
    }
}