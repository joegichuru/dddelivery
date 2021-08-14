package com.joe.dialdelivery.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joe.network.Api
import com.joe.network.model.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

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
            .subscribe({
                orders.postValue(it.data)
            }, {
                it.printStackTrace()
                error.postValue(true)
                loading.postValue(false)
            }, {
                //
                Log.i("done", "done")
                loading.postValue(false)
                // orderAdapter.notifyDataSetChanged()
            })
    }
}