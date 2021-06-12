package ca.chronofit.chrono.util.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ca.chronofit.chrono.R
import ca.chronofit.chrono.util.objects.CircuitObject
import com.google.android.material.textview.MaterialTextView

class ChapterItemAdapter(
    private val data: List<CircuitObject>,
    private val clickListener: (CircuitObject) -> Unit,
    private val menuClickListener: (Int) -> Unit,
    private val context: Context
) :
    RecyclerView.Adapter<ChapterItemAdapter.CircuitViewHolder>() {

    class CircuitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: MaterialTextView = itemView.findViewById(R.id.circuit_name)
        private val numSets: MaterialTextView = itemView.findViewById(R.id.num_sets)
        private val timeRest: MaterialTextView = itemView.findViewById(R.id.rest_time)
        private val timeWork: MaterialTextView = itemView.findViewById(R.id.work_time)
        private val icon: ImageView = itemView.findViewById(R.id.circuit_icon)
        private val more: ImageView = itemView.findViewById(R.id.more_menu)

        @SuppressLint("SetTextI18n")
        fun bind(
            circuit: CircuitObject,
            clickListener: (CircuitObject) -> Unit,
            onMenuClickListener: (Int) -> Unit,
            position: Int,
            context: Context
        ) {
            name.text = circuit.name

            if (circuit.sets == 1)
                numSets.text = circuit.sets.toString() + " Set"
            else
                numSets.text = circuit.sets.toString() + " Sets"

            timeRest.text = "Rest: " + circuit.rest.toString() + "s"
            timeWork.text = "Work: " + circuit.work.toString() + "s"

            // Set Circuit Icon
            val icons: TypedArray = context.resources.obtainTypedArray(R.array.icon_files)
            icon.setImageResource(
                context.resources.getIdentifier(
                    icons.getString(circuit.iconId!!),
                    "drawable",
                    context.packageName
                )
            )
            icons.recycle()
            itemView.setOnClickListener { clickListener(circuit) }
            more.setOnClickListener { onMenuClickListener(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircuitViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_circuit, parent, false)
        return CircuitViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CircuitViewHolder, position: Int) {
        val circuit = data[position]
        holder.bind(
            circuit,
            clickListener,
            menuClickListener,
            holder.layoutPosition,
            context
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }
}