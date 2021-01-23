package ca.chronofit.chrono.util

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ca.chronofit.chrono.util.constants.Constants
import ca.chronofit.chrono.util.objects.CircuitsObject
import ca.chronofit.chrono.util.objects.PreferenceManager

open class BaseActivity : AppCompatActivity() {
    private var circuits: CircuitsObject? = null

//    private var loadingAlert: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.with(this)

        circuits = PreferenceManager.get<CircuitsObject>(Constants.CIRCUITS)

        if (circuits == null) {
            circuits = CircuitsObject()
            PreferenceManager.put(circuits, Constants.CIRCUITS)
        }
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

//    fun showLoadingScreen() {
//        if (isContextValid(this)) {
//            val builder = AlertDialog.Builder(this)
//            val inflater = layoutInflater
//            val mView = inflater.inflate(R.layout.layout_loading, null) as RelativeLayout
//
//            builder.setView(mView)
//            builder.setCancelable(false)
//            loadingAlert = builder.create()
//            loadingAlert!!.setCanceledOnTouchOutside(false)
//            loadingAlert!!.show()
//            loadingAlert!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        }
//    }
//
//    fun hideLoadingScreen() {
//        if (loadingAlert != null && loadingAlert!!.isShowing) {
//            loadingAlert!!.dismiss()
//        }
//    }
}
