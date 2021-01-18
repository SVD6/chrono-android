package ca.chronofit.chrono.circuit

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
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.FragmentCircuitDashboardBinding
import ca.chronofit.chrono.util.BaseActivity
import ca.chronofit.chrono.util.adapters.CircuitViewAdapter
import ca.chronofit.chrono.util.objects.CircuitObject
import ca.chronofit.chrono.util.objects.CircuitsObject
import ca.chronofit.chrono.util.objects.PreferenceManager
import ca.chronofit.chrono.util.objects.SettingsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.dialog_alert.view.*
import kotlinx.android.synthetic.main.fragment_dashboard_bottom_sheet.view.*

class CircuitDashboardFrag : Fragment() {
    private lateinit var bind: FragmentCircuitDashboardBinding
    private lateinit var recyclerView: RecyclerView

    private val settingsViewModel: SettingsViewModel by activityViewModels()

    private var getReadyTime: Int? = null
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

        settingsViewModel.getReadyTime.observe(viewLifecycleOwner, { readyTime ->
            getReadyTime = (readyTime.substring(0, readyTime.length - 1)).toInt()
        })

        recyclerView = bind.recyclerView
        loadData()

        bind.addCircuit.setOnClickListener {
            startActivityForResult(Intent(requireContext(), CircuitCreate::class.java), 10001)
        }
        return bind.root
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
        intent.putExtra("readyTime", getReadyTime)
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
        val builder =
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomMaterialDialog).create()
        val dialogView = View.inflate(requireContext(), R.layout.dialog_alert, null)

        // Set Dialog Views
        dialogView.dialog_title.text =
            "Delete " + circuitsObject?.circuits!![position].name
        dialogView.subtitle.text = getString(R.string.delete_circuit_subtitle)
        dialogView.confirm.text = getString(R.string.delete)
        dialogView.cancel.text = getString(R.string.cancel)

        // Button Logic
        dialogView.cancel.setOnClickListener {
            builder.dismiss()
        }

        dialogView.confirm.setOnClickListener {
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
            bind.recyclerView.visibility = View.VISIBLE
            bind.emptyLayout.visibility = View.GONE

            recyclerView.adapter = CircuitViewAdapter(
                circuitsObject?.circuits!!,
                { circuitObject: CircuitObject -> circuitClicked(circuitObject) },
                { position: Int ->
                    circuitLongClicked(position)
                }, requireContext()
            )
        } else {
            loadEmptyUI()
        }
    }

    private fun loadEmptyUI() {
        bind.recyclerView.visibility = View.GONE
        bind.emptyLayout.visibility = View.VISIBLE
    }
}