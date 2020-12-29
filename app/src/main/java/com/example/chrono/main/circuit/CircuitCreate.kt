package com.example.chrono.main.circuit

import `in`.goodiebag.carouselpicker.CarouselPicker
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.example.chrono.R
import com.example.chrono.databinding.ActivityCircuitCreateBinding
import com.example.chrono.util.BaseActivity
import com.example.chrono.util.getDrawableByString
import com.example.chrono.util.getIconName
import com.example.chrono.util.objects.PreferenceManager
import com.example.chrono.util.objects.CircuitObject
import com.example.chrono.util.objects.CircuitsObject
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_circuit_icon.view.*

class CircuitCreate : BaseActivity() {

    private var bind: ActivityCircuitCreateBinding? = null
    private var selectedIcon: String = "ic_stopwatch"

    private val timeChangeVal: Int = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.with(this)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_circuit_create)

        bind!!.discardButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        bind!!.saveButton.setOnClickListener {
            if (validateInputs()) {
                saveCircuit()
            }
        }

        bind!!.circuitIcon.setOnClickListener {
            selectIconDialog()
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
        circuit.iconId = getDrawableByString(this, selectedIcon)

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
            bind!!.setNum.setText("1")
        } else {
            bind!!.setNum.setText((currentText.toInt() + 1).toString())
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
                bind!!.setNum.setText(((currentText.toInt() - 1)).toString())
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
            bind!!.setWorkTime.setText(timeChangeVal.toString())
        } else {
            bind!!.setWorkTime.setText(floorVal(currentText.toInt() + timeChangeVal).toString())
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
                bind!!.setWorkTime.setText(roundTimeDown(currentText.toInt()).toString())
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
            bind!!.setRestTime.setText(timeChangeVal.toString())
        } else {
            bind!!.setRestTime.setText(floorVal(currentText.toInt() + timeChangeVal).toString())
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
                bind!!.setRestTime.setText(roundTimeDown(currentText.toInt()).toString())
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

    private fun setIcon(selectedIcon: String) {
        // NOTE: Doesn't look great gotta fix up a bit
        bind!!.circuitIcon.setImageResource(getDrawableByString(this, selectedIcon))
    }

    private fun selectIconDialog() {
        val builder = MaterialAlertDialogBuilder(this).create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_circuit_icon, null)

        // Setting Carousel Items
        val imageItems: ArrayList<CarouselPicker.PickerItem> = ArrayList()
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_stopwatch))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_abs))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_arm))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_bottle))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_boxer))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_dumbbell))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gym))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gym_bag))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gym_2))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gym_3))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gym_4))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gymnast))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_jump_rope))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_mat))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_punching_ball))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_resistance))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_resistance_1))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_treadmill))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_workout))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_workout_3))

        val imageAdapter: CarouselPicker.CarouselViewAdapter =
            CarouselPicker.CarouselViewAdapter(this, imageItems, 0)
        dialogView.carousel.adapter = imageAdapter

        // Carousel Logic
        dialogView!!.carousel.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                selectedIcon = getIconName(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        // Button Logic
        dialogView.negative_button.setOnClickListener {
            builder.dismiss()
        }

        dialogView.positive_button.setOnClickListener {
            setIcon(selectedIcon)
            builder.dismiss()
        }

        // Display the Dialog
        builder.setView(dialogView)
        builder.show()
    }
}
