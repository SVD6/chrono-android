package com.example.chrono.main.timer

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import com.example.chrono.R
import com.example.chrono.databinding.ActivityCircuitCreateBinding
import com.example.chrono.util.BaseActivity
import com.example.chrono.util.PreferenceManager
import com.example.chrono.util.objects.CircuitObject
import com.example.chrono.util.objects.CircuitsObject

class CircuitCreate : BaseActivity() {

    private var bind: ActivityCircuitCreateBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.with(this)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_circuit_create)

        bind!!.nameInput.setStartIconOnClickListener {
            // Select icon :)
        }

        bind!!.discardButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        bind!!.saveButton.setOnClickListener {
            if (validateInputs()) {
                saveCircuit()
            }
        }

        bind!!.addSet.setOnClickListener {
            addSet()
        }
        bind!!.minusSet.setOnClickListener {
            minusSet()
        }
        bind!!.addWork.setOnClickListener {
            addWork()
        }
        bind!!.minusWork.setOnClickListener {
            minusWork()
        }
        bind!!.addRest.setOnClickListener {
            addRest()
        }
        bind!!.minusRest.setOnClickListener {
            minusRest()
        }
    }

    private fun saveCircuit() {
        val circuit = CircuitObject()
        circuit.name = bind!!.nameInput.editText!!.text.toString()
        circuit.sets = bind!!.setNum.text.toString().toInt()
        circuit.work = bind!!.setWorkTime.text.toString().toInt()
        circuit.rest = bind!!.setRestTime.text.toString().toInt()

        // Save circuit in Shared Preferences
        val circuits: CircuitsObject? = PreferenceManager.get<CircuitsObject>("CIRCUITS")
        circuits!!.circuits!!.add(circuit)
        PreferenceManager.put(circuits, circuits.key)

        Log.i("prefs", "" + PreferenceManager.get("CIRCUITS"))
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun validateInputs(): Boolean {
        if (bind!!.nameInput.isEmpty() or (bind!!.nameInput.editText!!.text.toString() == "")) {
            Toast.makeText(this, "Please enter a circuit name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (bind!!.setNum.text.toString().toInt() == 0) {
            Toast.makeText(this, "Number of sets must be greater than 0", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!(bind!!.setWorkTime.text.toString().matches(("^[1-9]\\d*\$").toRegex()))) {
            Toast.makeText(this, "Invalid Rest Time", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!(bind!!.setRestTime.text.toString().matches(("^[1-9]\\d*\$").toRegex()))) {
            Toast.makeText(this, "Invalid Rest Time", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun addSet() {
        if (bind!!.setNum.text.toString() == "") {
            bind!!.setNum.setText("1")
        } else {
            bind!!.setNum.setText((bind!!.setNum.text.toString().toInt() + 1).toString())
        }
    }

    private fun minusSet() {
        if (bind!!.setNum.text.toString() == "") {
            Toast.makeText(
                this,
                "Can't have a negative number of sets..? " + ("\uD83E\uDD14"),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (bind!!.setNum.text.toString().toInt() != 0) {
                bind!!.setNum.setText(
                    (bind!!.setNum.text.toString().toInt() - 1).toString()
                )
            }
        }
    }

    private fun addWork() {
        if (bind!!.setWorkTime.text.toString() == "") {
            bind!!.setWorkTime.setText("1")
        } else {
            bind!!.setWorkTime.setText((bind!!.setWorkTime.text.toString().toInt() + 1).toString())
        }
    }

    private fun minusWork() {
        if (bind!!.setWorkTime.text.toString() == "") {
            Toast.makeText(
                this,
                "Can't have a negative time for work " + ("\uD83D\uDE2C"),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (bind!!.setWorkTime.text.toString().toInt() != 0) {
                bind!!.setWorkTime.setText(
                    (bind!!.setWorkTime.text.toString().toInt() - 1).toString()
                )
            }
        }
    }

    private fun addRest() {
        if (bind!!.setRestTime.text.toString() == "") {
            bind!!.setRestTime.setText("1")
        } else {
            bind!!.setRestTime.setText((bind!!.setRestTime.text.toString().toInt() + 1).toString())
        }
    }

    private fun minusRest() {
        if (bind!!.setRestTime.text.toString() == "") {
            Toast.makeText(
                this,
                "Can't have a negative time for rest " + ("\uD83D\uDE2C"),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (bind!!.setRestTime.text.toString().toInt() != 0) {
                bind!!.setRestTime.setText(
                    (bind!!.setRestTime.text.toString().toInt() - 1).toString()
                )
            }
        }
    }
}
