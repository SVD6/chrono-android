package com.example.chrono

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.chrono.databinding.ActivityMainBinding
import com.example.chrono.util.BaseActivity

class MainActivity : BaseActivity() {
    var bind: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)

        bind!!.navtoggle.setOnClickListener {
            if (bind!!.navtoggle.isChecked) {
                bind!!.swatchlay.visibility = View.VISIBLE
                bind!!.timerlay.visibility = View.GONE
            } else {
                bind!!.swatchlay.visibility = View.GONE
                bind!!.timerlay.visibility = View.VISIBLE
            }
        }
    }
}
