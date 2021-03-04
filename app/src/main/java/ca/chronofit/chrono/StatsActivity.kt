package ca.chronofit.chrono

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import ca.chronofit.chrono.databinding.ActivityStatsBinding
import ca.chronofit.chrono.databinding.DialogAlertBinding
import ca.chronofit.chrono.util.BaseActivity
import ca.chronofit.chrono.util.constants.Constants
import ca.chronofit.chrono.util.objects.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
            PreferenceManager.put(firstLaunch, Constants.EASTER_EGG_DIALOG)
        }

        bind.back.setOnClickListener { finish() }
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