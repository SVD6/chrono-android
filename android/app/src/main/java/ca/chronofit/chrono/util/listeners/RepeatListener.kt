package ca.chronofit.chrono.util.listeners

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View

// Following: https://stackoverflow.com/questions/4284224/android-hold-button-to-repeat-action
class RepeatListener(clickListener: View.OnClickListener) : View.OnTouchListener {
    var handler = Handler(Looper.getMainLooper())
    private var onClickListener: View.OnClickListener? = clickListener
    private var touchView: View? = null

    @Volatile
    private var counter = 0

    private val runnable = object : Runnable {
        override fun run() {
            if (touchView!!.isEnabled) {
                if (counter > 0) {
                    handler.postDelayed(this, FAST_INCREMENT_PERIOD)
                } else {
                    handler.postDelayed(this, SLOW_INCREMENT_PERIOD)
                }
                counter++
                onClickListener!!.onClick(touchView)
            } else {
                handler.removeCallbacks(this)
                touchView!!.isPressed = false
                touchView = null
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, SLOW_INCREMENT_PERIOD)
                touchView = v
                touchView!!.isPressed = true
                onClickListener!!.onClick(touchView)
                return true
            }
            MotionEvent.ACTION_UP -> {
                handler.removeCallbacks(runnable)
                touchView!!.isPressed = false
                touchView = null
                counter = 0
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacks(runnable)
                touchView!!.isPressed = false
                touchView = null
                counter = 0
                return true
            }
        }
        return false
    }

    companion object {
        const val SLOW_INCREMENT_PERIOD = 500L // In ms
        const val FAST_INCREMENT_PERIOD = 100L // In ms
    }
}