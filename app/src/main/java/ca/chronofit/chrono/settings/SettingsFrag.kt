package ca.chronofit.chrono.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ca.chronofit.chrono.MainActivity
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.FragmentSettingsBinding
import ca.chronofit.chrono.util.constants.Constants
import ca.chronofit.chrono.util.objects.PreferenceManager
import ca.chronofit.chrono.util.objects.SettingsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.android.synthetic.main.dialog_dark_mode.view.*
import kotlinx.android.synthetic.main.dialog_ready_time.view.*
import java.lang.Exception

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
        defaultSettings()
        initMenus()

        return bind.root
    }

    private fun defaultSettings() {
        // Dark Mode Setting
        bind.darkModeDisplay.text =
            PreferenceManager.get(Constants.DARK_MODE_SETTING).replace("\"", "")

        // Notifications Setting
        if (PreferenceManager.get<Boolean>(Constants.NOTIFICATION_SETTING) == null) {
            switchLogic(true, bind.notificationSwitch)
            PreferenceManager.put(true, Constants.NOTIFICATION_SETTING)
        } else {
            switchLogic(
                PreferenceManager.get<Boolean>(Constants.NOTIFICATION_SETTING)!!,
                bind.notificationSwitch
            )
        }

        // Ready Time Setting
        if (PreferenceManager.get<Int>(Constants.GET_READY_SETTING) == null) {
            bind.readyTimeDisplay.text = getString(R.string._5s)
        } else {
            bind.readyTimeDisplay.text =
                (PreferenceManager.get<Int>(Constants.GET_READY_SETTING)!!.toString() + "s")
        }

        // Last Rest Setting
        if (PreferenceManager.get<Boolean>(Constants.LAST_REST_SETTING) == null) {
            bind.lastRestSwitch.isChecked = true
            PreferenceManager.put(true, Constants.LAST_REST_SETTING)
        } else {
            bind.lastRestSwitch.isChecked =
                PreferenceManager.get<Boolean>(Constants.LAST_REST_SETTING)!!
        }

        // Audio Setting
        if (PreferenceManager.get<Boolean>(Constants.AUDIO_SETTING) == null) {
            switchLogic(true, bind.audioSwitch)
            PreferenceManager.put(true, Constants.AUDIO_SETTING)
        } else {
            switchLogic(PreferenceManager.get<Boolean>(Constants.AUDIO_SETTING)!!, bind.audioSwitch)
        }
    }

    private fun initMenus() {
        // Dark Mode Popup
        bind.darkMode.setOnClickListener {
            showDarkModePopup()
        }

        // Notification Switch
        bind.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            switchLogic(isChecked, bind.notificationSwitch)
            PreferenceManager.put(isChecked, Constants.NOTIFICATION_SETTING)
        }

        // Get Ready Time Popup
        bind.getReadyTime.setOnClickListener {
            showReadyTimePopup()
        }

        // Audio Prompt Switch
        bind.audioSwitch.setOnCheckedChangeListener { _, isChecked ->
            switchLogic(isChecked, bind.audioSwitch)
            PreferenceManager.put(isChecked, Constants.AUDIO_SETTING)
        }

        // Last Rest Switch
        bind.lastRestSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settingsViewModel.onLastRestChanged(true)
                PreferenceManager.put(true, Constants.LAST_REST_SETTING)
            } else {
                settingsViewModel.onLastRestChanged(false)
                PreferenceManager.put(false, Constants.LAST_REST_SETTING)
            }
        }

        // Get Help Launch
        bind.getHelp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                val emailArray = arrayOf("support@chronofit.ca")
                putExtra(Intent.EXTRA_EMAIL, emailArray)
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.help_email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.help_email_body))
            }
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Log.d("SettingsFrag", e.message.toString())
            }
        }

        // Rate App Launch
        bind.rateApp.setOnClickListener {
            val packageName = requireContext().packageName
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }

        // Privacy Policy Launch
        bind.privacyPolicy.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://chronofit.herokuapp.com/privacyPolicy")
            )
            startActivity(intent)
        }

        bind.versionNumber.text = Constants.VERSION_NUMBER
    }

    private fun switchLogic(setting: Boolean, switch: SwitchMaterial) {
        if (setting) {
            switch.isChecked = true
            switch.text = getString(R.string.on)
        } else {
            switch.isChecked = false
            switch.text = getString(R.string.off)
        }
    }

    private fun showReadyTimePopup() {
        val builder =
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomMaterialDialog).create()
        val dialogView = View.inflate(requireContext(), R.layout.dialog_ready_time, null)

        // Preselect a Radio Button
        when (bind.readyTimeDisplay.text) {
            "5s" -> dialogView.ready_time_select.check(R.id.radio_5s)
            "10s" -> dialogView.ready_time_select.check(R.id.radio_10s)
            "15s" -> dialogView.ready_time_select.check(R.id.radio_15s)
        }

        // Radio Listener
        dialogView.ready_time_select.setOnCheckedChangeListener { _, _ ->
            val selectedTime =
                (dialogView.findViewById(dialogView.ready_time_select.checkedRadioButtonId) as RadioButton).text
            bind.readyTimeDisplay.text = selectedTime

            PreferenceManager.put(
                (selectedTime.toString().substring(0, selectedTime.toString().length - 1))
                    .toInt(), Constants.GET_READY_SETTING
            )
            settingsViewModel.onReadyTimeChanged(selectedTime.toString())
            builder.dismiss()
        }

        builder.setView(dialogView)
        builder.show()
    }

    private fun showDarkModePopup() {
        val builder =
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomMaterialDialog).create()
        val dialogView = View.inflate(requireContext(), R.layout.dialog_dark_mode, null)

        // Preselect a Radio Button
        when (bind.darkModeDisplay.text) {
            Constants.DARK_MODE -> dialogView.dark_mode_select.check(R.id.radio_on)
            Constants.LIGHT_MODE -> dialogView.dark_mode_select.check(R.id.radio_off)
            Constants.SYSTEM_DEFAULT -> dialogView.dark_mode_select.check(R.id.radio_system)
        }

        // Radio Listener
        dialogView.dark_mode_select.setOnCheckedChangeListener { _: RadioGroup, _: Int ->
            val selectedText =
                (dialogView.findViewById(dialogView.dark_mode_select.checkedRadioButtonId) as RadioButton).text
            bind.darkModeDisplay.text = selectedText

            PreferenceManager.put(selectedText, Constants.DARK_MODE_SETTING)
            settingsViewModel.onDarkModeChanged(selectedText.toString())
            builder.dismiss()
        }

        builder.setView(dialogView)
        builder.show()
    }
}
