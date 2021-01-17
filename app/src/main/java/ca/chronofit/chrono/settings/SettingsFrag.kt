package ca.chronofit.chrono.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.FragmentSettingsBinding
import ca.chronofit.chrono.MainActivity
import ca.chronofit.chrono.util.objects.PreferenceManager
import ca.chronofit.chrono.util.objects.SettingsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.dialog_alert.view.cancel
import kotlinx.android.synthetic.main.dialog_alert.view.confirm
import kotlinx.android.synthetic.main.dialog_ready_time.view.*

class SettingsFrag : Fragment() {
    private lateinit var bind: FragmentSettingsBinding

    private val settingsViewModel: SettingsViewModel by activityViewModels()

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

        // Dark Mode Not Ready
        bind.darkMode.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Dark Mode under construction \uD83D\uDEA7",
                Toast.LENGTH_SHORT
            ).show()
        }

        return bind.root
    }

    private fun initMenus() {
        // Load Options

        bind.privacyPolicy.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://chronofit.herokuapp.com/privacyPolicy")
            )
            startActivity(intent)
        }

        // Dark Mode Switch
        bind.darkModeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.text = getString(R.string.on)
                settingsViewModel.onDarkModeChanged(true)
            } else {
                buttonView.text = getString(R.string.off)
                settingsViewModel.onDarkModeChanged(false)
            }
        }

        // Notification Switch
        bind.notificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.text = getString(R.string.on)
                settingsViewModel.onNotificationChanged(true)
            } else {
                buttonView.text = getString(R.string.off)
                settingsViewModel.onNotificationChanged(false)
            }
        }

        // Audio Prompt Switch
        bind.audioSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.text = getString(R.string.on)
                settingsViewModel.onAudioPromptChanged(true)
            } else {
                buttonView.text = getString(R.string.off)
                settingsViewModel.onAudioPromptChanged(false)
            }
        }

        // Get Ready Time Selector
        bind.getReadyTime.setOnClickListener {
            val builder =
                MaterialAlertDialogBuilder(requireContext(), R.style.CustomMaterialDialog).create()
            val dialogView = View.inflate(requireContext(), R.layout.dialog_ready_time, null)

            // Pre select the saved option (if it's 5s then make sure 5s is already checked)
            // For now it's default 5s -> make sure to change this once load settings is there
            // Button Logic
            dialogView.cancel.setOnClickListener {
                builder.dismiss()
            }

            dialogView.confirm.setOnClickListener {
                builder.dismiss()
                val selectedTime =
                    (dialogView.findViewById(dialogView.ready_time_select.checkedRadioButtonId) as RadioButton).text
                bind.readyTimeDisplay.text = selectedTime
                // Add to Settings that yo new get ready time
                settingsViewModel.onReadyTimeChanged(selectedTime.toString())
            }

            builder.setView(dialogView)
            builder.show()
        }
    }
}