package ca.chronofit.chrono.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import ca.chronofit.chrono.R
import ca.chronofit.chrono.databinding.ActivityOnboardBinding
import ca.chronofit.chrono.util.helpers.ZoomOutPageTransformer

class OnBoardActivity : AppCompatActivity() {
    private lateinit var bind: ActivityOnboardBinding
    private var welcomeFrag: Fragment = OnBoardWelcomeFrag()
    private var circuitFrag: Fragment = OnBoardCircuitFrag()
    private var swatchFrag: Fragment = OnBoardSwatchFrag()
    private var settingFrag: Fragment = OnBoardSettingFrag()
    private var finalFrag: Fragment = OnBoardFinalFrag()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_onboard)

        val adapter = OnBoardAdapter(supportFragmentManager)
        bind.pager.adapter = adapter
        bind.pager.offscreenPageLimit = 5
        bind.pager.setPageTransformer(true, ZoomOutPageTransformer())

        bind.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    3 -> {
                        bind.skip.visibility = View.VISIBLE
                        bind.next.visibility = View.VISIBLE
                        bind.continueButton.visibility = View.GONE
                    }
                    4 -> {
                        bind.skip.visibility = View.GONE
                        bind.next.visibility = View.GONE
                        bind.continueButton.visibility = View.VISIBLE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        bind.next.setOnClickListener { nextPage() }
        bind.skip.setOnClickListener { skip() }

        // after everything is done so last fragment

        // change next to say "continue"

        // when continue is pressed -> startActivity(MainActivity)
    }

    private fun nextPage() {
        bind.pager.currentItem++
    }

    private fun skip() {
        bind.pager.currentItem = bind.pager.adapter!!.count - 1
    }

    private inner class OnBoardAdapter constructor(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> welcomeFrag
                1 -> circuitFrag
                2 -> swatchFrag
                3 -> settingFrag
                else -> finalFrag
            }
        }

        override fun getCount(): Int {
            return 5
        }
    }
}