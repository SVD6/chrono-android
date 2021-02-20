package ca.chronofit.chrono.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.ActivityOnboardBinding
import ca.chronofit.chrono.util.helpers.ZoomOutPageTransformer

class OnBoardActivity : AppCompatActivity() {
    private lateinit var bind: ActivityOnboardBinding
    private var welcomeFrag: Fragment = OnBoardWelcomeFrag()
    private var circuitFrag: Fragment = OnBoardCircuitFrag()
    private var swatchFrag: Fragment = OnBoardSwatchFrag()
    private var finalFrag: Fragment = OnBoardFinalFrag()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_onboard)

        val adapter = OnBoardAdapter(supportFragmentManager)
        bind.pager.adapter = adapter
        bind.pager.offscreenPageLimit = 4
        bind.pager.setPageTransformer(true, ZoomOutPageTransformer())

        // after everything is done so last fragment

        // change next to say "continue"

        // when continue is pressed -> startActivity(MainActivity)
    }

    private inner class OnBoardAdapter constructor(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> welcomeFrag
                1 -> circuitFrag
                2 -> swatchFrag
                else -> finalFrag
            }
        }

        override fun getCount(): Int {
            return 4
        }
    }
}