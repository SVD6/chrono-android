package com.example.chrono.main.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chrono.R
import com.example.chrono.util.objects.CircuitObject

class TimerActivity(private val circuit: CircuitObject) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
    }
}