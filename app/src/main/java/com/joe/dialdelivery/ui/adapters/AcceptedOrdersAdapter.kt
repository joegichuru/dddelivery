package com.joe.dialdelivery.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.joe.dialdelivery.R
import com.joe.dialdelivery.ui.fragments.formatDate
import com.joe.network.model.Order

class AcceptedOrdersAdapter(val context: Context, val orders: MutableList<Order>) :
    RecyclerView.Adapter<AcceptedOrdersAdapter.AcceptedOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptedOrderViewHolder {
        return AcceptedOrderViewHolder(
            LayoutInflater.from(context).inflate(R.layout.accpted_order, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AcceptedOrderViewHolder, position: Int) {
        val order = orders[position]
        holder.id.text = "#${order.id}"
        holder.createdAt.text = formatDate(order.createdAt)
        holder.title.text = order.title
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class AcceptedOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.order_id)
        val title: TextView = itemView.findViewById(R.id.title)
        val createdAt: TextView = itemView.findViewById(R.id.created_at)
    }
}