package com.joe.dialdelivery.ui.fragments

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.joe.dialdelivery.databinding.FragmentHomeBinding
import com.joe.dialdelivery.ui.adapters.OrderAdapter
import com.joe.dialdelivery.ui.adapters.OrderCallback
import com.joe.dialdelivery.ui.viewmodels.AcceptedOrderViewModel
import com.joe.dialdelivery.ui.viewmodels.OrderViewModel
import com.joe.network.Api
import com.joe.network.ApiClient
import com.joe.network.model.Order
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.log
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */

class HomeFragment : Fragment(), OrderCallback {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var orderAdapter: OrderAdapter

    private val compositeDisposable = CompositeDisposable()
    val acceptedOrderViewModel: AcceptedOrderViewModel by activityViewModels()
    val ordersViewModel: OrderViewModel by activityViewModels()

    //track all running disposables
    private val disposables: MutableList<Disposable> = mutableListOf()
    val orders = mutableListOf<Order>()
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        mediaPlayer = MediaPlayer.create(requireContext(), alarmUri)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //bind layout
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.errorView.setOnClickListener {
            findOrders()
        }
        ordersViewModel.orders.observe(viewLifecycleOwner, {
            orders.clear()
            orders.addAll(it)
            binding.progressBar.visibility = View.GONE
            orderAdapter.notifyDataSetChanged()
        })
        ordersViewModel.error.observe(viewLifecycleOwner, {
            if (it) {
                binding.errorView.visibility = View.VISIBLE
            } else {
                binding.errorView.visibility = View.GONE
            }
        })
        ordersViewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        //setup adapter
        orderAdapter = OrderAdapter(orders, requireContext(), this)
        binding.ordersList.adapter = orderAdapter
    }

    private fun findOrders() {
        ordersViewModel.fetchOrders()
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
        disposables.add(disposable)
    }

    override fun onReject(position: Int) {
        ordersViewModel.removeOrder(orders[position])
        compositeDisposable.remove(disposables[position])
        orderAdapter.notifyItemRemoved(position)
    }

    override fun onAccept(position: Int) {
        acceptedOrderViewModel.addOrder(orders[position])
        ordersViewModel.removeOrder(orders[position])
        compositeDisposable.remove(disposables[position])
        orderAdapter.notifyItemRemoved(position)
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
            it.progress.progress = progress
            it.timeLeft.text = timeLeft(order.expiredAt)
            if (progress <= 0) {
                //hide accept btn
                //show expireb btn
                it.acceptBtn.visibility = View.GONE
                it.expiredBtn.visibility = View.VISIBLE
            } else {
                alertUser(order.alertedAt)
                it.acceptBtn.visibility = View.VISIBLE
                it.expiredBtn.visibility = View.GONE
            }
        }
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
        Log.i("resume", "resume")
        super.onResume()

    }

    private fun playTone() {
        mediaPlayer.start()
    }

    private fun alertUser(alertTime: Date) {
        val now =Calendar.getInstance()
        if (now.timeInMillis>alertTime.time) {
            playTone()
        }
    }
}

fun progress(expiresAt: Date, createdAt: Date): Int {
    val now = Calendar.getInstance().time
    if (now > expiresAt) {
        //expired
        return 0;
    }
    val duration = ((expiresAt.time - createdAt.time)).toDouble()
    val remaining = ((expiresAt.time - now.time)).toDouble()
    return ((remaining / duration) * 100).roundToInt()
}

fun timeLeft(expiresAt: Date): String {
    val now = Calendar.getInstance().time
    if (now > expiresAt) {
        //expired
        return "Order expired.";
    }
    val remaining = ((expiresAt.time - now.time))
    val minutes = remaining / 60000
    val seconds = (remaining % 60000) / 1000
    return "Expires in $minutes min $seconds sec."
}

fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("hh:mm a")
    return dateFormat.format(date)
}