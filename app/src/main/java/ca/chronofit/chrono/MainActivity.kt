package ca.chronofit.chrono

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import ca.chronofit.chrono.databinding.ActivityMainBinding
import ca.chronofit.chrono.circuit.CircuitDashboardFrag
import ca.chronofit.chrono.settings.SettingsFrag
import ca.chronofit.chrono.stopwatch.StopwatchFrag
import ca.chronofit.chrono.util.BaseActivity
import ca.chronofit.chrono.util.objects.SettingsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var bind: ActivityMainBinding

    private val settingsViewModel: SettingsViewModel by viewModels()

    private lateinit var frag1: StopwatchFrag
    private lateinit var frag2: CircuitDashboardFrag
    private lateinit var frag3: SettingsFrag

    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val fragTransaction = supportFragmentManager.beginTransaction()

        frag1 = StopwatchFrag()
        fragTransaction.add(R.id.content, frag1)

        frag2 = CircuitDashboardFrag()
        fragTransaction.add(R.id.content, frag2)

        frag3 = SettingsFrag()
        fragTransaction.add(R.id.content, frag3)

        fragTransaction.commitAllowingStateLoss()

        bind.navBar.setOnNavigationItemSelectedListener(this)
        bind.navBar.selectedItemId = R.id.nav_circuit

        createTimerNotificationChannel()
        createStopwatchNotificationChannel()
        // Observe Settings
        observeSettings()

        // Check for an Update

        // Check for App Review
    }

    private fun observeSettings() {
        settingsViewModel.darkMode.observe(this, { darkMode ->
            if (darkMode) {
                Log.i("settings", "Registered darkMode")
            } else {
                Log.i("settings", "Unregistered darkMode")
            }
        })

        settingsViewModel.notifications.observe(this, { notifications ->
            if (notifications) {
                Log.i("settings", "Registered notifications")
            } else {
                Log.i("settings", "Unregistered notifications")
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragTransaction = supportFragmentManager.beginTransaction()

        when (item.itemId) {
            R.id.nav_stopwatch -> {
                fragTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                fragTransaction.hide(frag2).hide(frag3).show(frag1).commitNow()
                return true
            }
            R.id.nav_circuit -> {
                if (frag3.isVisible) {
                    fragTransaction.setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_right
                    )
                } else {
                    fragTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                }
                fragTransaction.hide(frag1).hide(frag3).show(frag2).commitNow()
                return true
            }
            else -> {
                fragTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                fragTransaction.hide(frag1).hide(frag2).show(frag3).commitNow()
                return true
            }
        }
    }

    override fun onBackPressed() {
        if (frag2.isHidden) {
            bind.navBar.selectedItemId = R.id.nav_circuit
        } else {
            super.onBackPressed()
        }
    }

    private fun createTimerNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.timer_notification_channel_id)
            val descriptionText = "Timer notification"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(name, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createStopwatchNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.stopwatch_notification_channel_id)
            val descriptionText = "Stopwatch notification"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(name, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}