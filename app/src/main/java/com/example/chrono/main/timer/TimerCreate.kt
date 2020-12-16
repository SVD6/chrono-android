package com.example.chrono.main.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.chrono.R
import com.example.chrono.databinding.ActivityTimerCreateBinding

class TimerCreate : AppCompatActivity() {

    var bind: ActivityTimerCreateBinding? = null
//    var carousel: CarouselPicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = DataBindingUtil.setContentView(this, R.layout.activity_timer_create)
//        carousel = bind!!.carousel
//
//        var icon_list: ArrayList<CarouselPicker.PickerItem> = ArrayList()
//        icon_list.add(CarouselPicker.DrawableItem(R.drawable.ic_abs))
//        icon_list.add(CarouselPicker.DrawableItem(R.drawable.ic_abs))
//        icon_list.add(CarouselPicker.DrawableItem(R.drawable.ic_abs))
//        icon_list.add(CarouselPicker.DrawableItem(R.drawable.ic_abs))
//        icon_list.add(CarouselPicker.DrawableItem(R.drawable.ic_abs))
//        icon_list.add(CarouselPicker.DrawableItem(R.drawable.ic_abs))
//        icon_list.add(CarouselPicker.DrawableItem(R.drawable.ic_abs))
//
//        val adapter: CarouselPicker.CarouselViewAdapter = CarouselPicker.CarouselViewAdapter(this, icon_list, 0)
//        carousel!!.adapter = adapter

    }
}
