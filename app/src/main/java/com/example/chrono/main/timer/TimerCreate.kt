package com.example.chrono.main.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import com.example.chrono.R
import com.example.chrono.databinding.ActivityTimerCreateBinding
import com.example.chrono.util.objects.CircuitObject

class TimerCreate : AppCompatActivity() {

    var bind: ActivityTimerCreateBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_timer_create)

        bind!!.nameInput.setStartIconOnClickListener {
            // Select icon :)
        }

        bind!!.discardButton.setOnClickListener {
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
        Toast.makeText(
            this,
            "" + circuit.name + " " + circuit.sets + " " + circuit.work + " " + circuit.rest,
            Toast.LENGTH_LONG
        ).show()
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
        bind!!.setNum.text = (bind!!.setNum.text.toString().toInt() + 1).toString()
    }

    private fun minusSet() {
        if (bind!!.setNum.text.toString().toInt() != 0) {
            bind!!.setNum.text = (bind!!.setNum.text.toString().toInt() - 1).toString()
        }
    }

    private fun addWork() {
        bind!!.setWorkTime.setText((bind!!.setWorkTime.text.toString().toInt() + 1).toString())
    }

    private fun minusWork() {
        if (bind!!.setWorkTime.text.toString().toInt() != 0) {
            bind!!.setWorkTime.setText((bind!!.setWorkTime.text.toString().toInt() - 1).toString())
        }
    }

    private fun addRest() {
        bind!!.setRestTime.setText((bind!!.setRestTime.text.toString().toInt() + 1).toString())
    }

    private fun minusRest() {
        if (bind!!.setRestTime.text.toString().toInt() != 0) {
            bind!!.setRestTime.setText((bind!!.setRestTime.text.toString().toInt() - 1).toString())
        }
    }


}
