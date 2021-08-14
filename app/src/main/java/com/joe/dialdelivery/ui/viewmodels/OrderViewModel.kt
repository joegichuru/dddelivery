package com.joe.dialdelivery.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joe.network.Api
import com.joe.network.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class OrderViewModel @Inject constructor(private val api: Api) : ViewModel() {
    var orders: MutableLiveData<List<Order>> = MutableLiveData()
    val error: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        fetchOrders()
    }

    fun removeOrder(order: Order) {
        orders.value = orders.value?.filter { it != order }
    }

    fun fetchOrders() {
        api.getOrders()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                error.postValue(false)
                loading.postValue(true)
            }
            .subscribe({ it ->
                //stream data to change create and expiry times to
                // be atleast around this time to simulate a realistic order
                orders.postValue(it.data.map {order->
                    val time = (2..4).random()
                    val now = Calendar.getInstance()
                    val expireAt = Calendar.getInstance()
                    val alertAt = Calendar.getInstance()
                    expireAt.add(Calendar.MINUTE, time)
                    alertAt.add(Calendar.MINUTE, time / 2)
                    order.alertedAt = alertAt.time
                    order.expiredAt = expireAt.time
                    order.createdAt = now.time
                    order
                })
            }, {
                error.postValue(true)
                loading.postValue(false)
            }, {
                loading.postValue(false)

            })
    }
}