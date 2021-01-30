package ca.chronofit.chrono.util.components

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import java.util.*

class Chronometer2_0 @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : AppCompatTextView(
    context!!, attrs, defStyle
) {
    private var seconds = 0
    private var running = false
    lateinit var runnable: Runnable


    private fun updateText(time: String) {
        text = time
    }

    fun start() {
        running = true
        var handler = Handler(Looper.getMainLooper())

        runnable = Runnable {
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val secs = seconds % 60
            val time = java.lang.String
                .format(
                    Locale.getDefault(),
                    "%d:%02d:%02d", hours,
                    minutes, secs
                )

            updateText(time)

            if (running){
                seconds ++
            }

            handler.postDelayed(runnable, 1000)
        }

        handler.post(runnable)
    }
}