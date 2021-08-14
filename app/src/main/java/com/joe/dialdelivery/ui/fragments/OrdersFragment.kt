package com.joe.dialdelivery.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joe.dialdelivery.R
import com.joe.dialdelivery.databinding.FragmentOrdersBinding
import com.joe.dialdelivery.ui.adapters.AcceptedOrdersAdapter
import com.joe.dialdelivery.ui.viewmodels.AcceptedOrderViewModel
import com.joe.network.model.Order

/**
 * A simple [Fragment] subclass.
 * Use the [OrdersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrdersFragment : Fragment() {
    lateinit var binding: FragmentOrdersBinding
    val acceptedOrderViewModel: AcceptedOrderViewModel by activityViewModels()
    lateinit var ordersAdapter: AcceptedOrdersAdapter
    val orders: MutableList<Order> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        ordersAdapter = AcceptedOrdersAdapter(requireContext(), orders)
        binding.ordersList.adapter = ordersAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        acceptedOrderViewModel.acceptedOrders.observe(viewLifecycleOwner, {
            orders.addAll(it)
            ordersAdapter.notifyDataSetChanged()
        })

    }
}