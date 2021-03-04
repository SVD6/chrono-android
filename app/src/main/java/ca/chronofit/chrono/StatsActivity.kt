package ca.chronofit.chrono

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import ca.chronofit.chrono.databinding.ActivityStatsBinding
import ca.chronofit.chrono.databinding.DialogAlertBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class StatsActivity : AppCompatActivity() {
    private lateinit var bind: ActivityStatsBinding
    private var firstLaunch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_stats)

        if (firstLaunch) {
            showEasterEggDialog()
            firstLaunch = false
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