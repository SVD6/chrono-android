package com.example.chrono.main.timer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.chrono.R
import com.example.chrono.databinding.FragmentCircuitDashboardBinding
import com.example.chrono.util.BaseActivity
import com.example.chrono.util.PreferenceManager
import com.example.chrono.util.adapters.CircuitViewAdapter
import com.example.chrono.util.objects.CircuitsObject

class CircuitDashboardFrag : Fragment() {
    private var bind: FragmentCircuitDashboardBinding? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(
            inflater, R.layout.fragment_circuit_dashboard,
            container, false
        )
        PreferenceManager.with(activity as BaseActivity)

        recyclerView = bind!!.recyclerView
        loadData()

        bind!!.addCircuit.setOnClickListener {
            startActivityForResult(Intent(requireContext(), CircuitCreate::class.java), 10001)
        }

        return bind!!.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10001 && resultCode == Activity.RESULT_OK) {
            Log.i("gangshit", "gangshit")
            loadData()
        }
    }

    private fun loadData() {
        val dataSet = PreferenceManager.get<CircuitsObject>("CIRCUITS")
        Log.i("gang", "" + dataSet)
        if (dataSet != null) {
            Log.i("gang", "" + dataSet.circuits.toString())
            recyclerView.adapter = CircuitViewAdapter(dataSet.circuits!!)

        } else {
            loadEmptyUI()
        }
    }

    private fun loadEmptyUI() {
        TODO("Not yet implemented")
    }
}