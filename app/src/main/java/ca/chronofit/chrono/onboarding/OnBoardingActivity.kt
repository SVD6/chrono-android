package ca.chronofit.chrono.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.ActivityOnboardingBinding

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var bind: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)

        // after everything is done so last fragment

        // change next to say "continue"

        // when continue is pressed -> startActivity(MainActivity)
    }
}