package com.joe.dialdelivery.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joe.dialdelivery.R
import com.joe.network.model.AddOn

/**
 * Adapter for addons in a single order
 */
class AddOnAdapter(val context: Context, private val addons: List<AddOn>) :
    RecyclerView.Adapter<AddOnAdapter.AddOnViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOnViewHolder {
        return AddOnViewHolder(
            LayoutInflater.from(context).inflate(R.layout.add_on, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddOnViewHolder, position: Int) {
        val addOn = addons[position]
        holder.bindAddOn(addOn)
    }

    override fun getItemCount(): Int {
        return addons.size
    }

    inner class AddOnViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.id)
        val title: TextView = itemView.findViewById(R.id.title)
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        fun bindAddOn(addOn: AddOn) {
            id.text = "#${addOn.id}"
            title.text = addOn.title
            quantity.text = "x${addOn.quantity}"
        }
    }
}