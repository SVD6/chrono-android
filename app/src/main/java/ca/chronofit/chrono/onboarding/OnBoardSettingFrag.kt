package ca.chronofit.chrono.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.FragmentOnboardSettingBinding
import com.bumptech.glide.Glide

class OnBoardSettingFrag : Fragment() {
    private lateinit var bind: FragmentOnboardSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bind =
            DataBindingUtil.inflate(inflater, R.layout.fragment_onboard_setting, container, false)

        Glide.with(requireContext()).asGif().load(R.raw.onboard_dark).into(bind.imageGif)

        return bind.root
    }
}