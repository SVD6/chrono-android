package ca.chronofit.chrono.main.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.FragmentSettingsBinding
import ca.chronofit.chrono.main.MainActivity
import ca.chronofit.chrono.util.objects.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_alert.view.cancel
import kotlinx.android.synthetic.main.dialog_alert.view.confirm
import kotlinx.android.synthetic.main.dialog_ready_time.view.*

class SettingsFrag : Fragment() {

    private lateinit var bind: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings,
            container, false
        )

        PreferenceManager.with(activity as MainActivity)

        // Load Settings (they're either default or have been messed with a bit)

        initMenus()

        bind.darkModeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.text = getString(R.string.on)
            } else {
                buttonView.text = getString(R.string.off)
            }
        }

        bind.notificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.text = getString(R.string.on)
            } else {
                buttonView.text = getString(R.string.off)
            }
        }

        bind.audioSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.text = getString(R.string.on)
            } else {
                buttonView.text = getString(R.string.off)
            }
        }

        bind.getReadyTime.setOnClickListener {
            val builder =
                MaterialAlertDialogBuilder(requireContext(), R.style.CustomMaterialDialog).create()
            val dialogView = View.inflate(requireContext(), R.layout.dialog_ready_time, null)

            // Pre select the saved option (if it's 5s then make sure 5s is already checked)
            // For now it's default 5s
            // Button Logic
            dialogView.cancel.setOnClickListener {
                builder.dismiss()
            }

            dialogView.confirm.setOnClickListener {
                builder.dismiss()
                val button = dialogView.findViewById(dialogView.ready_time_select.checkedRadioButtonId) as RadioButton
                bind.readyTimeDisplay.text = button.text
                // Add to Settings that yo new get ready time
            }

            builder.setView(dialogView)
            builder.show()
        }

        return bind.root
    }

    private fun initMenus() {

    }
}