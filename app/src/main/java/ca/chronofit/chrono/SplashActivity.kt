package ca.chronofit.chrono

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import ca.chronofit.chrono.databinding.ActivitySplashBinding
import ca.chronofit.chrono.onboarding.OnBoardActivity
import ca.chronofit.chrono.util.BaseActivity
import ca.chronofit.chrono.util.constants.Constants
import ca.chronofit.chrono.util.objects.PreferenceManager

class SplashActivity : BaseActivity() {
    private lateinit var bind: ActivitySplashBinding // Bind variable for the activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        val prefs = getSharedPreferences("ca.chronofit.chrono", MODE_PRIVATE)

        if (prefs.getBoolean(Constants.FIRST_RUN, true)) {
            prefs.edit().putBoolean(Constants.FIRST_RUN, false).apply()
            PreferenceManager.put(Constants.SYSTEM_DEFAULT, Constants.DARK_MODE_SETTING)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            delegate.applyDayNight()
        } else {
            when (PreferenceManager.get(Constants.DARK_MODE_SETTING).replace("\"", "")) {
                Constants.DARK_MODE -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    delegate.applyDayNight()
                }
                Constants.LIGHT_MODE -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    delegate.applyDayNight()
                }
                Constants.SYSTEM_DEFAULT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    delegate.applyDayNight()
                }
            }
        }

        // Change the splash screen based on light/dark mode
        if (isUsingNightModeResources()) {
            bind.lightMode.visibility = View.GONE
            bind.darkMode.visibility = View.VISIBLE
        } else {
            bind.lightMode.visibility = View.VISIBLE
            bind.darkMode.visibility = View.GONE
        }

        // Set the time out delay and launch main activity afterwards
        Handler(
            Looper.getMainLooper()
        ).postDelayed(
            {
                startActivity(Intent(this, OnBoardActivity::class.java))
                finish()
            }, SPLASH_TIMEOUT
        )
    }

    companion object {
        private const val SPLASH_TIMEOUT = 3000L // Timeout delay
    }
}