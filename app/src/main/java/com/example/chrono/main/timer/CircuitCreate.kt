package com.example.chrono.main.timer

import android.app.Activity
import android.os.Bundle
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

    private val timeChangeVal: Int = 5

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

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun validateInputs(): Boolean {
        if (bind!!.nameInput.isEmpty() or (bind!!.nameInput.editText!!.text.toString() == "")) {
            Toast.makeText(this, "Please enter a circuit name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!(bind!!.setWorkTime.text.toString().matches(("^[1-9]\\d*\$").toRegex()))) {
            Toast.makeText(this, "Invalid Number of Sets", Toast.LENGTH_SHORT).show()
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
        val currentText = bind!!.setNum.text.toString()
        if (currentText == "") {
            setSetNum(1)
        } else {
            setSetNum(currentText.toInt() + 1)
        }
    }

    private fun minusSet() {
        val currentText = bind!!.setNum.text.toString()
        if (currentText == "") {
            Toast.makeText(
                this,
                "Can't have no sets! " + ("\uD83E\uDD14"),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (currentText.toInt() > 0) {
                setSetNum((currentText.toInt() - 1))
            } else {
                Toast.makeText(
                    this,
                    "Can't have a negative number of sets..? " + ("\uD83E\uDD14"),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addWork() {
        val currentText = bind!!.setWorkTime.text.toString()
        if (currentText == "") {
            setWorkTime(timeChangeVal)
        } else {
            setWorkTime(floorVal(currentText.toInt() + timeChangeVal))
        }
    }

    private fun minusWork() {
        val currentText = bind!!.setWorkTime.text.toString()
        if (currentText == "") {
            Toast.makeText(
                this,
                "Can't have no work time! " + ("\uD83D\uDE2C"),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (currentText.toInt() > 0) {
                setWorkTime(roundTimeDown(currentText.toInt()))
            } else {
                Toast.makeText(
                    this,
                    "Can't have a negative time for work " + ("\uD83D\uDE2C"),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addRest() {
        val currentText = bind!!.setRestTime.text.toString()
        if (currentText == "") {
            setRestTime(timeChangeVal)
        } else {
            setRestTime(floorVal(currentText.toInt() + timeChangeVal))
        }
    }

    private fun minusRest() {
        val currentText = bind!!.setRestTime.text.toString()
        if (currentText == "") {
            Toast.makeText(
                this,
                "Can't have no rest! " + ("\uD83D\uDE2C"),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (currentText.toInt() > 0) {
                setRestTime(roundTimeDown(currentText.toInt()))
            } else {
                Toast.makeText(
                    this,
                    "Can't have a negative time for rest " + ("\uD83D\uDE2C"),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Floor to a multiple of timeChangeVal
    private fun floorVal(valToFloor: Int): Int {
        return (valToFloor / timeChangeVal) * (timeChangeVal)
    }

    //When decrementing set time, subtract to the closest multiple of timeChangeVal
    private fun roundTimeDown(valToRound: Int): Int {
        return if (valToRound % timeChangeVal == 0) {
            valToRound - timeChangeVal
        } else {
            floorVal(valToRound)
        }
    }

    //Change SetNum textView text
    private fun setSetNum(newVal: Int) {
        bind!!.setNum.setText(newVal.toString())
    }

    //Change SetRestTime textView text
    private fun setRestTime(newTime: Int) {
        bind!!.setRestTime.setText(newTime.toString())
    }


    // Change SetWorkTime textView text
    private fun setWorkTime(newTime: Int) {
        bind!!.setWorkTime.setText(newTime.toString())
    }


}
