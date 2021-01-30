package ca.chronofit.chrono.circuit

import `in`.goodiebag.carouselpicker.CarouselPicker
import android.app.Activity
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.ActivityCircuitCreateBinding
import ca.chronofit.chrono.util.BaseActivity
import ca.chronofit.chrono.util.constants.Constants
import ca.chronofit.chrono.util.constants.Events
import ca.chronofit.chrono.util.objects.CircuitObject
import ca.chronofit.chrono.util.objects.CircuitsObject
import ca.chronofit.chrono.util.objects.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.dialog_select_icon.view.*

class CircuitCreate : BaseActivity() {
    private lateinit var bind: ActivityCircuitCreateBinding

    private var selectedIcon: Int = 0
    private lateinit var iconNames: TypedArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.with(this)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_circuit_create)

        iconNames = resources.obtainTypedArray(R.array.icon_files)

        bind.discardButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        bind.saveButton.setOnClickListener {
            if (validateInputs()) {
                FirebaseAnalytics.getInstance(this).logEvent(Events.CREATE_COMPLETE, Bundle())
                saveCircuit()
            }
        }

        bind.circuitName.addTextChangedListener {
            if (bind.circuitName.length() >= MAX_CHARACTERS) {
                bind.circuitNameWarning.visibility = View.VISIBLE
                bind.circuitName.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.stop_red))
            } else {
                bind.circuitNameWarning.visibility = View.GONE
                bind.circuitName.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent))
            }
        }

        bind.iconLayout.setOnClickListener {
            hideKeyboard(currentFocus ?: View(this))
            selectIconDialog()
        }
        bind.circuitIcon.setOnClickListener {
            hideKeyboard(currentFocus ?: View(this))
            selectIconDialog()
        }
        bind.addSet.setOnClickListener {
            addSet()
        }
        bind.minusSet.setOnClickListener {
            minusSet()
        }
        bind.addWork.setOnClickListener {
            addWork()
        }
        bind.minusWork.setOnClickListener {
            minusWork()
        }
        bind.addRest.setOnClickListener {
            hideKeyboard(currentFocus ?: View(this))
            addRest()
        }
        bind.minusRest.setOnClickListener {
            hideKeyboard(currentFocus ?: View(this))
            minusRest()
        }
    }

    private fun saveCircuit() {
        val circuit = CircuitObject()
        circuit.name = bind.circuitName.text.toString()
        circuit.sets = bind.setNum.text.toString().toInt()
        circuit.work = bind.setWorkTime.text.toString().toInt()
        circuit.rest = bind.setRestTime.text.toString().toInt()
        circuit.iconId = selectedIcon

        // Save circuit in Shared Preferences
        val circuits: CircuitsObject? = PreferenceManager.get<CircuitsObject>(Constants.CIRCUITS)
        circuits!!.circuits!!.add(circuit)
        PreferenceManager.put(circuits, Constants.CIRCUITS)

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun validateInputs(): Boolean {
        if (bind.circuitName.text.toString() == "") {
            Toast.makeText(this, "Please enter a circuit name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!(bind.setNum.text.toString().matches(("^[1-9]\\d*\$").toRegex()))) {
            Toast.makeText(this, "Invalid Number of Sets.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!(bind.setWorkTime.text.toString().matches(("^[1-9]\\d*\$").toRegex()))) {
            Toast.makeText(this, "Invalid Work Time.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!(bind.setRestTime.text.toString().matches(("^[1-9]\\d*\$").toRegex()))) {
            Toast.makeText(this, "Invalid Rest Time", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun addSet() {
        val currentText = bind.setNum.text.toString()
        if (currentText == "") {
            bind.setNum.setText("1")
        } else {
            if (currentText.toInt() == MAX_SETS) {
                Toast.makeText(
                    this,
                    "Can't have more than 99 sets, take it easy!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                bind.setNum.setText((currentText.toInt() + 1).toString())
            }
        }
    }

    private fun minusSet() {
        val currentText = bind.setNum.text.toString()
        if (currentText == "") {
            Toast.makeText(
                this,
                "Can't have no sets! " + ("\uD83E\uDD14"),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (currentText.toInt() > 0) {
                bind.setNum.setText(((currentText.toInt() - 1)).toString())
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
        val currentText = bind.setWorkTime.text.toString()
        if (currentText == "") {
            bind.setWorkTime.setText(TIME_CHANGE_VALUE.toString())
        } else {
            if (currentText.toInt() >= MAX_WORK) {
                Toast.makeText(
                    this,
                    "Can't have more than 999 seconds of workout!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                bind.setWorkTime.setText(floorVal(currentText.toInt() + TIME_CHANGE_VALUE).toString())
            }
        }
    }

    private fun minusWork() {
        val currentText = bind.setWorkTime.text.toString()
        if (currentText == "") {
            Toast.makeText(
                this,
                "Can't have no work time! " + ("\uD83D\uDE2C"),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (currentText.toInt() > 0) {
                bind.setWorkTime.setText(roundTimeDown(currentText.toInt()).toString())
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
        val currentText = bind.setRestTime.text.toString()
        if (currentText == "") {
            bind.setRestTime.setText(TIME_CHANGE_VALUE.toString())
        } else {
            if (currentText.toInt() >= MAX_REST) {
                Toast.makeText(
                    this,
                    "Can't have more than 999 seconds of rest!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                bind.setRestTime.setText(floorVal(currentText.toInt() + TIME_CHANGE_VALUE).toString())
            }
        }
    }

    private fun minusRest() {
        val currentText = bind.setRestTime.text.toString()
        if (currentText == "") {
            Toast.makeText(
                this,
                "Can't have no rest! " + ("\uD83D\uDE2C"),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (currentText.toInt() > 0) {
                bind.setRestTime.setText(roundTimeDown(currentText.toInt()).toString())
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
        return (valToFloor / TIME_CHANGE_VALUE) * (TIME_CHANGE_VALUE)
    }

    //When decrementing set time, subtract to the closest multiple of timeChangeVal
    private fun roundTimeDown(valToRound: Int): Int {
        return if (valToRound % TIME_CHANGE_VALUE == 0) {
            valToRound - TIME_CHANGE_VALUE
        } else {
            floorVal(valToRound)
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun selectIconDialog() {
        val builder =
            MaterialAlertDialogBuilder(this, R.style.CustomMaterialDialog).create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_select_icon, null)

        // Setting Carousel Items
        val imageItems: ArrayList<CarouselPicker.PickerItem> = ArrayList()
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_default_icon))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_abs))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_arm))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_bottle))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_heart))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_dumbbell))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_machine))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_bench_press))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gym))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_bike_machine))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gym_bag))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gymnast))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_jump_rope))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_mat))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_whistle))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_resistance))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_grippers))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_trampoline))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_treadmill))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_workout))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_outdoor))

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
                selectedIcon = position
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        // Button Logic
        dialogView.dismiss.setOnClickListener {
            builder.dismiss()
        }

        dialogView.save.setOnClickListener {
            bind.circuitIcon.setImageResource(
                resources.getIdentifier(
                    iconNames.getString(selectedIcon),
                    "drawable",
                    packageName
                )
            )
            builder.dismiss()
        }

        // Display the Dialog
        builder.setView(dialogView)
        builder.show()
    }

    companion object {
        private const val MAX_SETS: Int = 99
        private const val MAX_WORK: Int = 995 // Actually 999
        private const val MAX_REST: Int = 995 // Actually 999
        private const val TIME_CHANGE_VALUE: Int = 5
        private const val MAX_CHARACTERS: Int = 50
    }
}
