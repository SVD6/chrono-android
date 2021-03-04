package ca.chronofit.chrono

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import ca.chronofit.chrono.databinding.ActivityStatsBinding

class StatsActivity : AppCompatActivity() {
    private lateinit var bind: ActivityStatsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_stats)
    }
}