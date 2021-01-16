package ca.chronofit.chrono.main.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.FragmentSettingsBinding

class SettingsFrag : Fragment() {

    private lateinit var bind: FragmentSettingsBinding

    private var darkModeOptions: List<String> = listOf("System Default", "On", "Off")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings,
            container, false
        )

        initMenus()

        return bind.root
    }

    private fun initMenus() {

        // Dark Mode Menu
        val adapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, darkModeOptions)
        bind.darkModeField.setAdapter(adapter)
    }
}