package com.joe.dialdelivery.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joe.network.model.Order
import dagger.hilt.EntryPoint


class AcceptedOrderViewModel : ViewModel() {
    val acceptedOrders = MutableLiveData<MutableList<Order>>(mutableListOf())

    fun addOrder(order: Order) {
        val orders = acceptedOrders.value
        orders?.add(order)
        acceptedOrders.value=orders
    }
}