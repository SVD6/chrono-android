package com.example.chrono.main.timer

import `in`.goodiebag.carouselpicker.CarouselPicker
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.chrono.R
import com.example.chrono.databinding.FragmentCircuitDashboardBinding
import com.example.chrono.util.BaseActivity
import com.example.chrono.util.objects.PreferenceManager
import com.example.chrono.util.adapters.CircuitViewAdapter
import com.example.chrono.util.objects.CircuitsObject

class CircuitDashboardFrag : Fragment() {
    private var bind: FragmentCircuitDashboardBinding? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(
            inflater, R.layout.fragment_circuit_dashboard,
            container, false
        )
        PreferenceManager.with(activity as BaseActivity)

        recyclerView = bind!!.recyclerView
        loadData()

        bind!!.addCircuit.setOnClickListener {
            startActivityForResult(Intent(requireContext(), CircuitCreate::class.java), 10001)
        }

        return bind!!.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10001 && resultCode == Activity.RESULT_OK) {
            loadData()
        }
    }

    private fun loadData() {
        val dataSet = PreferenceManager.get<CircuitsObject>("CIRCUITS")
        if ((dataSet != null) and (dataSet!!.circuits!!.size > 0)) {
            bind!!.recyclerView.visibility = View.VISIBLE
            bind!!.emptyLayout.visibility = View.GONE

            recyclerView.adapter = CircuitViewAdapter(dataSet.circuits!!)
        } else {
            loadEmptyUI()
        }
    }

    private fun loadEmptyUI() {
        bind!!.recyclerView.visibility = View.GONE
        bind!!.emptyLayout.visibility = View.VISIBLE

        // Carousel stuff get rid of it later.
        var imageItems: ArrayList<CarouselPicker.PickerItem> = ArrayList()
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_abs))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_arm))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_bottle))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_boxer))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_dumbbell))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gym))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gym_bag))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gym_2))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gym_3))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gym_4))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_gymnast))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_jump_rope))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_mat))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_punching_ball))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_resistance))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_resistance_1))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_treadmill))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_workout))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.ic_workout_3))

        val imageAdapter: CarouselPicker.CarouselViewAdapter =
            CarouselPicker.CarouselViewAdapter(context, imageItems, 0)
        bind!!.carousel.adapter = imageAdapter

        bind!!.carousel.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
//                var resID = resources.getIdentifier()
                bind!!.iconName.text = position.toString()
            }

            override fun onPageSelected(position: Int) {
                print("hi")
            }

            override fun onPageScrollStateChanged(state: Int) {
                print("hi")
            }

        })
    }
}