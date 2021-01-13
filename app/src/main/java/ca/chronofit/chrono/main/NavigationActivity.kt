package ca.chronofit.chrono.main

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.ActivityNavigationBinding
import ca.chronofit.chrono.main.circuit.CircuitDashboardFrag
import ca.chronofit.chrono.main.settings.SettingsFrag
import ca.chronofit.chrono.main.stopwatch.StopwatchFrag
import ca.chronofit.chrono.util.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bind: ActivityNavigationBinding

    private lateinit var frag1: StopwatchFrag
    private lateinit var frag2: CircuitDashboardFrag
    private lateinit var frag3: SettingsFrag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_navigation)

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
                fragTransaction.show(frag1)
                fragTransaction.hide(frag2)
                fragTransaction.hide(frag3).commitAllowingStateLoss()
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
                fragTransaction.show(frag2)
                fragTransaction.hide(frag1)
                fragTransaction.hide(frag3).commitAllowingStateLoss()
                return true
            }
            else -> {
                fragTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                fragTransaction.show(frag3)
                fragTransaction.hide(frag1)
                fragTransaction.hide(frag2).commitAllowingStateLoss()
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
}