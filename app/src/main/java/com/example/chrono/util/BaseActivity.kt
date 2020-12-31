package com.example.chrono.util

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chrono.util.objects.CircuitsObject
import com.example.chrono.util.objects.PreferenceManager

open class BaseActivity : AppCompatActivity() {

    private var circuits: CircuitsObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.with(this)

        circuits = PreferenceManager.get<CircuitsObject>("CIRCUITS")

        if (circuits == null) {
            circuits = CircuitsObject()
            PreferenceManager.put(circuits, "CIRCUITS")
        }
        // The code to mess around with the status bar, only works on KitKat and onwards
        // If it's after KitKat we change the color of the status bar, else we just make it invisible
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            val w: Window = window
//            w.setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            )
//            val decor = window.decorView
//            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        } else {
//            setStatusBarInvisible()
//        }
    }

    public override fun onSaveInstanceState(state: Bundle) {
        state.putBoolean("StateSaved", true)
        super.onSaveInstanceState(state)
    }

    // The function that checks if dark mode is enabled on a device, used in all the activities
    fun isUsingNightModeResources(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }
}
