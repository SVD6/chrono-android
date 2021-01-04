package com.example.chrono.main.circuit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.dialog_circuit_delete.view.*
import kotlinx.android.synthetic.main.fragment_dashboard_bottom_sheet.view.*

class CircuitDashboardFrag : Fragment() {
    private var bind: FragmentCircuitDashboardBinding? = null
    private lateinit var recyclerView: RecyclerView

    private var circuitsObject: CircuitsObject? = null
    private var selectedPosition: Int = 0

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
            Log.i("circuit_activity", "Completed a circuit.")
        }
    }

    private fun circuitClicked(circuit: CircuitObject) {
        val jsonString = GsonBuilder().create().toJson(circuit)
        val intent = Intent(requireContext(), CircuitTimerActivity::class.java)
        intent.putExtra("circuitObject", jsonString)
        startActivityForResult(intent, 10002)
    }

    @SuppressLint("InflateParams")
    private fun circuitLongClicked(position: Int) {
        selectedPosition = position

        // Roll out the bottom sheet
        val modalSheetView = layoutInflater.inflate(R.layout.fragment_dashboard_bottom_sheet, null)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(modalSheetView)

        modalSheetView.delete_layout.setOnClickListener {
            deleteCircuit(dialog, position)
        }

        modalSheetView.edit_layout.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "\uD83D\uDEE0\uFE0F Edit circuit coming soon!! \uD83D\uDEE0\uFE0F",
                Toast.LENGTH_SHORT
            ).show()
        }

        modalSheetView.share_layout.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "\uD83D\uDEE0\uFE0F Share a circuit coming soon!! \uD83D\uDEE0\uFE0F",
                Toast.LENGTH_SHORT
            ).show()
        }
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun deleteCircuit(dialog: BottomSheetDialog, position: Int) {
        val builder = MaterialAlertDialogBuilder(requireContext()).create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_circuit_delete, null)

        dialogView.delete_circuit_name.text =
            "Delete " + circuitsObject?.circuits!![position].name

        // Button Logic
        dialogView.cancel_button.setOnClickListener {
            builder.dismiss()
        }

        dialogView.delete_button.setOnClickListener {
            // Dismiss popups
            builder.dismiss()
            dialog.dismiss()

            // Remove from model and recyclerview
            circuitsObject?.circuits?.remove(circuitsObject?.circuits!![position])
            recyclerView.adapter?.notifyItemRemoved(position)
            recyclerView.adapter?.notifyItemRangeChanged(position, circuitsObject?.circuits!!.size)

            if (recyclerView.adapter?.itemCount!! == 0) {
                loadEmptyUI()
            }

            // Save updated list in local storage
            PreferenceManager.put(circuitsObject, "CIRCUITS")
        }

        // Display the Dialog
        builder.setView(dialogView)
        builder.show()
    }

    private fun loadData() {
        circuitsObject = PreferenceManager.get<CircuitsObject>("CIRCUITS")

        if (circuitsObject != null && circuitsObject?.circuits!!.size > 0) {
            bind!!.recyclerView.visibility = View.VISIBLE
            bind!!.emptyLayout.visibility = View.GONE

            recyclerView.adapter = CircuitViewAdapter(
                circuitsObject?.circuits!!,
                { circuitObject: CircuitObject -> circuitClicked(circuitObject) },
                { position: Int ->
                    circuitLongClicked(position)
                },
            )
        } else {
            loadEmptyUI()
        }
    }

    private fun loadEmptyUI() {
        bind!!.recyclerView.visibility = View.GONE
        bind!!.emptyLayout.visibility = View.VISIBLE
    }
}