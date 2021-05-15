package ca.chronofit.chrono

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
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

        PreferenceManager.with(this)

        // Get Boolean preference if it's first time app launch
        var isFirstRun = PreferenceManager.get<Boolean>(Constants.FIRST_RUN)

        if (isFirstRun == null || isFirstRun) {
            isFirstRun = true
            // Set dark mode value to system default
            PreferenceManager.put(false, Constants.FIRST_RUN)
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
            window.statusBarColor = Color.BLACK
        } else {
            bind.lightMode.visibility = View.VISIBLE
            bind.darkMode.visibility = View.GONE
            window.statusBarColor = ContextCompat.getColor(this, R.color.gradient_start)
        }

        // Set the time out delay and launch main activity afterwards
        Handler(
            Looper.getMainLooper()
        ).postDelayed(
            {
                launchActivity(isFirstRun)
            }, SPLASH_TIMEOUT
        )
    }

    private fun launchActivity(isFirst: Boolean) {
        if (isFirst) {
            startActivity(Intent(this, OnBoardActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }

    companion object {
        private const val SPLASH_TIMEOUT = 1500L // Timeout delay
    }
}