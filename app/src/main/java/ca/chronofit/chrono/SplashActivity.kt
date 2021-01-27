package ca.chronofit.chrono

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import ca.chronofit.chrono.databinding.ActivitySplashBinding
import ca.chronofit.chrono.util.BaseActivity
import ca.chronofit.chrono.util.constants.Constants
import ca.chronofit.chrono.util.objects.PreferenceManager

private const val SPLASH_TIMEOUT = 3000L // Timeout delay

class SplashActivity : BaseActivity() {
    private var bind: ActivitySplashBinding? = null // Bind variable for the activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_splash)

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

        // Change the splash screen based on light/dark mode
        if (isUsingNightModeResources()) {
            bind!!.lightMode.visibility = View.GONE
            bind!!.darkMode.visibility = View.VISIBLE
        } else {
            bind!!.lightMode.visibility = View.VISIBLE
            bind!!.darkMode.visibility = View.GONE
        }

        val shared = getSharedPreferences("TEST", MODE_PRIVATE)
        val editor = shared.edit()
        editor.putString("notification", "on").apply()

        // Set the time out delay and launch main activity afterwards
        Handler(
            Looper.getMainLooper()
        ).postDelayed(
            {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, SPLASH_TIMEOUT
        )
    }
}