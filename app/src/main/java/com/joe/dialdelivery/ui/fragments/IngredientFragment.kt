package com.joe.dialdelivery.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joe.dialdelivery.R
import com.joe.dialdelivery.databinding.FragmentIngredientBinding
import com.joe.dialdelivery.ui.adapters.IngredientAdapter
import com.joe.network.ApiClient
import com.joe.network.model.Recipe
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.operators.single.SingleDoOnDispose
import io.reactivex.rxjava3.schedulers.Schedulers

const val ID = "id"

class IngredientFragment : Fragment() {
    private lateinit var binding: FragmentIngredientBinding
    private lateinit var disposable: Disposable
    private lateinit var adapter: IngredientAdapter
    private val ingredients: MutableList<Recipe> = mutableListOf()
    private var id: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentIngredientBinding.inflate(inflater, container, false)
        adapter = IngredientAdapter(ingredients, requireContext())
        binding.ingredientList.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findIngredient()
    }

    private fun findIngredient() {
        disposable = ApiClient.provideApiClient(requireContext()).getIngredients(id!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                ingredients.addAll(it.data)
            }, {
                it.printStackTrace()
            }, {
                adapter.notifyDataSetChanged()
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int) =
            IngredientFragment().apply {
                arguments = Bundle().apply {
                    putInt("id", id)
                }
            }
    }

}