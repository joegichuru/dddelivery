package com.joe.dialdelivery.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joe.dialdelivery.R
import com.joe.dialdelivery.databinding.FragmentHomeBinding
import com.joe.dialdelivery.ui.adapters.DisposableCallback
import com.joe.dialdelivery.ui.adapters.OrderAdapter
import com.joe.network.ApiClient
import com.joe.network.model.Order
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.round
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), DisposableCallback {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var orderAdapter: OrderAdapter

    private val compositeDisposable = CompositeDisposable()
    val orders = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.i("date",Date().to)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //bind layout
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }
    private fun initAdapter(){
        //setup adapter
        orderAdapter = OrderAdapter(orders, requireContext(), this)
        binding.ordersList.adapter = orderAdapter
    }
    private fun findOrders(){
        ApiClient.provideApiClient(requireContext()).getOrders()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({

                       orders.addAll(it.data)
            },{
              it.printStackTrace()
            },{
                //
                Log.i("done","done")
                orderAdapter.notifyDataSetChanged()
            })
    }

    override fun publish(position: Int) {
        //use position to register a disposable
        //check if order expired before registering
        val disposable =
            Observable.interval(1, TimeUnit.SECONDS).delay(0, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    updateItem(position, it)
                }
        compositeDisposable.add(disposable)
    }

    private fun updateItem(position: Int, lo: Long) {
        val vh =
            binding.ordersList.findViewHolderForAdapterPosition(position) as OrderAdapter.OrderViewHolder?
        vh?.let {
            //check if expired first if expired mark as expired and disable accept button
            //of not expired count the minutes remaining
            //Remaining=expireAt-now

            val order = orders[position]
            // Log.i("order",order.toString())
            val progress = progress(order.expiredAt, order.createdAt)
            Log.i("progress","$progress")
            it.progress.progress=progress
            if (progress <= 0) {
                it.acceptBtn.text="Expired"
            }
            it.timeLeft.text = " some #$lo"
        }

        //  orderAdapter.notifyItemChanged(position,order)

    }

    private fun progress(expiresAt: Date, createdAt: Date): Int {
        val now = Calendar.getInstance().time
        if (now > expiresAt) {
            Log.i("exp","expired")
            //expired
            return 0;
        }
        val duration = ((expiresAt.time - createdAt.time) ).toDouble()
        val remaining = ((expiresAt.time - now.time) ).toDouble()
        Log.i("dur","$duration")
        Log.i("rem","$remaining")
        return ((remaining / duration) * 100).roundToInt()
    }

    //create dummy orders
    private fun createOrders(): MutableList<Order> {
        val now = Calendar.getInstance()
        val expiresAt = Calendar.getInstance()
        expiresAt.add(Calendar.MINUTE, 1)
        return mutableListOf(
            Order(1, createdAt = now.time, expiredAt = expiresAt.time),
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    override fun onResume() {
        super.onResume()
        findOrders()
    }
}