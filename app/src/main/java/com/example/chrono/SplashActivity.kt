package com.example.chrono

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.chrono.databinding.ActivitySplashBinding
import com.example.chrono.main.MainActivity
import com.example.chrono.util.BaseActivity

class SplashActivity : BaseActivity() {
    var bind: ActivitySplashBinding? = null // Bind variable for the activity
    private val SPLASH_TIME_OUT = 3000L // Timeout delay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        // Change the splash screen based on light/dark mode
        if (isUsingNightModeResources()) {
            bind!!.lightmode.visibility = View.GONE
            bind!!.darkmode.visibility = View.VISIBLE
        } else {
            bind!!.lightmode.visibility = View.VISIBLE
            bind!!.darkmode.visibility = View.GONE
        }

        // Set the time out delay and launch main activity afterwards
        Handler().postDelayed(
            {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, SPLASH_TIME_OUT
        )
    }
}