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
import com.joe.dialdelivery.ui.fragments.formatDate
import com.joe.dialdelivery.ui.fragments.progress
import com.joe.dialdelivery.ui.fragments.timeLeft
import com.joe.network.model.Order

class OrderAdapter(
    private val orders: MutableList<Order>,
    private val context: Context,
    private val orderCallback: OrderCallback
) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.order, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.orderId.text = "#${order.id}"
        holder.title.text=order.title
        val progress = progress(order.expiredAt, order.createdAt)
        holder.progress.progress = progress
        holder.timeLeft.text = timeLeft(order.expiredAt)
        holder.createdAt.text = formatDate(order.createdAt)
        if (progress <= 0) {
            //hide accept btn
            //show expired btn
            holder.acceptBtn.visibility = View.GONE
            holder.expiredBtn.visibility = View.VISIBLE
        } else {
            holder.acceptBtn.visibility = View.VISIBLE
            holder.expiredBtn.visibility = View.GONE
        }
        val adapter=AddOnAdapter(context,order.addOns)
        holder.addOns.adapter=adapter
        //should register an observer timer
        orderCallback.publish(position)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeLeft: TextView = itemView.findViewById(R.id.time_left)
        val orderId: TextView = itemView.findViewById(R.id.order_id)
        val progress: LinearProgressIndicator = itemView.findViewById(R.id.progress_horizontal)
        val acceptBtn: TextView = itemView.findViewById(R.id.accept_btn)
        val expiredBtn: TextView = itemView.findViewById(R.id.expired_btn)
        val createdAt: TextView = itemView.findViewById(R.id.created_at)
        val addOns: RecyclerView = itemView.findViewById(R.id.add_ons)
        val title:TextView=itemView.findViewById(R.id.title)

        init {
            expiredBtn.setOnClickListener {
                orderCallback.onReject(adapterPosition)
            }
            acceptBtn.setOnClickListener {
                orderCallback.onAccept(adapterPosition)
            }
        }

    }
}

interface OrderCallback {
    fun publish(position: Int)
    fun onReject(position: Int)
    fun onAccept(position: Int)
}