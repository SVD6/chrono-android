package ca.chronofit.chrono

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import ca.chronofit.chrono.databinding.ActivityStatsBinding
import ca.chronofit.chrono.databinding.DialogAlertBinding
import ca.chronofit.chrono.util.BaseActivity
import ca.chronofit.chrono.util.constants.Constants
import ca.chronofit.chrono.util.constants.Events
import ca.chronofit.chrono.util.objects.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.analytics.FirebaseAnalytics
import java.math.BigDecimal

class StatsActivity : BaseActivity() {
    private lateinit var bind: ActivityStatsBinding
    private var firstLaunch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_stats)
        PreferenceManager.with(this)

        // Retrieve boolean value from preference manager
        if (PreferenceManager.get<Boolean>(Constants.EASTER_EGG_DIALOG) == null) {
            PreferenceManager.put(true, Constants.EASTER_EGG_DIALOG)
        } else {
            firstLaunch = PreferenceManager.get<Boolean>(Constants.EASTER_EGG_DIALOG)!!
        }

        if (firstLaunch) {
            showEasterEggDialog()
            firstLaunch = false
            FirebaseAnalytics.getInstance(this).logEvent(Events.EASTER_EGG_FOUND, Bundle())
            PreferenceManager.put(firstLaunch, Constants.EASTER_EGG_DIALOG)
        }

        getStats()

        bind.back.setOnClickListener { finish() }
    }

    private fun getStats() {
        // Total circuits stat setting
        if (PreferenceManager.get<Int>(Constants.TOTAL_CIRCUITS) == null) {
            PreferenceManager.put(0, Constants.TOTAL_CIRCUITS)
            bind.totalCircuits.text = getString(R.string._0)
        } else {
            bind.totalCircuits.text =
                PreferenceManager.get<Int>(Constants.TOTAL_CIRCUITS).toString()
        }

        // Total time stat setting
        if (PreferenceManager.get<Int>(Constants.TOTAL_TIME) == null) {
            PreferenceManager.put(BigDecimal(0), Constants.TOTAL_TIME)
            bind.totalTime.text = getString(R.string._0h0m0s)
        } else {
            val seconds = PreferenceManager.get<Int>(Constants.TOTAL_TIME)
            bind.totalTime.text = convertSeconds(BigDecimal(seconds!!))
        }
    }

    private fun convertSeconds(seconds: BigDecimal): String {
        return if (seconds == BigDecimal(0)) {
            "0h 0m 0s"
        } else {
            val longVal: Long = seconds.toLong()
            val hours = longVal.toInt() / 3600
            var remainder = longVal.toInt() - hours * 3600
            val mins = remainder / 60
            remainder -= mins * 60
            val secs = remainder
            "${hours}h ${mins}m ${secs}s"
        }
    }

    private fun showEasterEggDialog() {
        val builder =
            MaterialAlertDialogBuilder(this, R.style.CustomMaterialDialog).create()
        val dialogBinding = DialogAlertBinding.inflate(LayoutInflater.from(this))

        // Set Dialog Views
        dialogBinding.dialogTitle.text = getString(R.string.easter_egg)
        dialogBinding.dialogSubtitle.text = getString(R.string.easter_egg_subtitle)
        dialogBinding.confirm.visibility = View.GONE
        dialogBinding.cancel.visibility = View.GONE

        // Show Dialog
        builder.setView(dialogBinding.root)
        builder.show()
    }
}