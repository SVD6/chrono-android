package ca.chronofit.chrono.main

import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.ActivityMainBinding
import ca.chronofit.chrono.main.circuit.CircuitDashboardFrag
import ca.chronofit.chrono.main.settings.SettingsFrag
import ca.chronofit.chrono.main.stopwatch.StopwatchFrag
import ca.chronofit.chrono.util.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bind: ActivityMainBinding

    private lateinit var frag1: StopwatchFrag
    private lateinit var frag2: CircuitDashboardFrag
    private lateinit var frag3: SettingsFrag

    private lateinit var notificationManager: NotificationManager

    private var mLastDayNightMode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mLastDayNightMode = AppCompatDelegate.getDefaultNightMode()

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

        // Check for an Update

        // Check for App Review
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

    override fun onRestart() {
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()
//        recreate()
    }

    override fun onBackPressed() {
        if (frag2.isHidden) {
            bind.navBar.selectedItemId = R.id.nav_circuit
        } else {
            super.onBackPressed()
        }
    }
}