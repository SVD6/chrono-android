package com.example.chrono.main.circuit

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
import com.example.chrono.util.adapters.CircuitViewAdapter
import com.example.chrono.util.objects.CircuitObject
import com.example.chrono.util.objects.CircuitsObject
import com.example.chrono.util.objects.PreferenceManager
import com.google.gson.GsonBuilder

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
            loadData()
        }
        if (requestCode == 10002 && resultCode == Activity.RESULT_OK) {
            // Ideal spot to ask for a rating after a threshold of timers have been run
            Log.i("result", "Success Worked")
        }
    }

    private fun circuitClicked(circuit: CircuitObject) {
        val jsonString = GsonBuilder().create().toJson(circuit)
        val intent = Intent(requireContext(), CircuitTimerActivity::class.java)
        intent.putExtra("circuitObject", jsonString)
        startActivityForResult(intent, 10002)
    }

    private fun loadData() {
        val dataSet = PreferenceManager.get<CircuitsObject>("CIRCUITS")
        if ((dataSet != null) and (dataSet!!.circuits!!.size > 0)) {
            bind!!.recyclerView.visibility = View.VISIBLE
            bind!!.emptyLayout.visibility = View.GONE

            recyclerView.adapter = CircuitViewAdapter(
                dataSet.circuits!!
            ) { circuitObject: CircuitObject -> circuitClicked(circuitObject) }
        } else {
            loadEmptyUI()
        }
    }

    private fun loadEmptyUI() {
        bind!!.recyclerView.visibility = View.GONE
        bind!!.emptyLayout.visibility = View.VISIBLE
    }
}