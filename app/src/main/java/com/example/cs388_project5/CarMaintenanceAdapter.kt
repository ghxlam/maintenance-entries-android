package com.example.cs388_project5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class CarMaintenanceAdapter(
    private val onDeleteClick: (CarMaintenance) -> Unit
) : RecyclerView.Adapter<CarMaintenanceAdapter.CarMaintenanceViewHolder>() {

    private var maintenanceList: List<CarMaintenance> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarMaintenanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car_maintenance, parent, false)
        return CarMaintenanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarMaintenanceViewHolder, position: Int) {
        holder.bind(maintenanceList[position])
    }

    override fun getItemCount(): Int = maintenanceList.size

    fun setMaintenanceList(newList: List<CarMaintenance>) {
        val diffCallback = MaintenanceDiffCallback(maintenanceList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        maintenanceList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class CarMaintenanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        private val textViewMiles: TextView = itemView.findViewById(R.id.textViewMiles)
        private val textViewCost: TextView = itemView.findViewById(R.id.textViewCost)
        private val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)

        fun bind(maintenance: CarMaintenance) {
            textViewName.text = maintenance.name
            textViewDescription.text = maintenance.description
            textViewMiles.text = "${maintenance.miles} miles"
            textViewCost.text = "$${maintenance.cost}"

            buttonDelete.setOnClickListener {
                onDeleteClick(maintenance)
            }
        }
    }

    class MaintenanceDiffCallback(
        private val oldList: List<CarMaintenance>,
        private val newList: List<CarMaintenance>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}

