package com.joe.dialdelivery.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.joe.dialdelivery.R
import com.joe.network.model.Order

class OrderAdapter(
    private val orders: MutableList<Order>,
    private val context: Context,
    private val disposableCallback: DisposableCallback
) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeLeft: TextView = itemView.findViewById(R.id.time_left)
        val orderId: TextView = itemView.findViewById(R.id.order_id)
        val progress:LinearProgressIndicator=itemView.findViewById(R.id.progress_horizontal)
        val acceptBtn:Button=itemView.findViewById(R.id.accept_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.order, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.orderId.text = "#${order.id}"
        holder.timeLeft.text ="#${order.progress}"
        //  holder.
        //should register an observer timer
        disposableCallback.publish(position)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

}

interface DisposableCallback {
    fun publish(position: Int)
}