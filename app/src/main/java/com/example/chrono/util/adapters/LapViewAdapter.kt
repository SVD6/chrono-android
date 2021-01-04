package com.example.chrono.util.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chrono.R
import com.example.chrono.util.getTime
import com.example.chrono.util.objects.LapObject
import com.google.android.material.textview.MaterialTextView

class LapViewAdapter(private val data: List<LapObject>) :
    RecyclerView.Adapter<LapViewAdapter.LapViewHolder>() {

    class LapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lapNum: MaterialTextView = itemView.findViewById(R.id.lapNum)
        private val lapTime: MaterialTextView = itemView.findViewById(R.id.lapTime)
        private val totalTime: MaterialTextView = itemView.findViewById(R.id.totalTime)

        @SuppressLint("SetTextI18n")
        fun bind(lap: LapObject) {
            lapNum.text = lap.lapNum.toString()
            lapTime.text = getTime(lap.lapTime)
            totalTime.text = getTime(lap.totalTime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.lap_lay, parent, false)
        return LapViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        val lap = data[position]
        holder.bind(lap)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}