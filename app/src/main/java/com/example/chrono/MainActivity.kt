package com.example.chrono

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.chrono.databinding.ActivityMainBinding
import com.example.chrono.util.BaseActivity

class MainActivity : BaseActivity() {
    var bind: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}
