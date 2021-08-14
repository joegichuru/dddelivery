package com.joe.dialdelivery.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.joe.dialdelivery.R
import com.joe.dialdelivery.databinding.FragmentIngredientBinding
import com.joe.dialdelivery.ui.adapters.IngredientAdapter
import com.joe.dialdelivery.ui.viewmodels.IngredientViewModel
import com.joe.network.ApiClient
import com.joe.network.model.Recipe
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.operators.single.SingleDoOnDispose
import io.reactivex.rxjava3.schedulers.Schedulers

const val ID = "id"

class IngredientFragment : Fragment() {
    private lateinit var binding: FragmentIngredientBinding
    private lateinit var adapter: IngredientAdapter
    private val ingredients: MutableList<Recipe> = mutableListOf()
    private val ingredientViewModel: IngredientViewModel by activityViewModels()
    private var id: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(ID)
        }
        findIngredient()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentIngredientBinding.inflate(inflater, container, false)
        adapter = IngredientAdapter(ingredients, requireContext())
        binding.ingredientList.adapter = adapter
        binding.errorView.setOnClickListener {
            findIngredient()
        }
        ingredientViewModel.ingredients.observe(viewLifecycleOwner, {
            ingredients.clear()
            ingredients.addAll(it)
            adapter.notifyDataSetChanged()
        })
        ingredientViewModel.error.observe(viewLifecycleOwner, {
            if (it) {
                if (binding.errorView.visibility != View.VISIBLE) {
                    binding.errorView.visibility = View.VISIBLE
                }

            } else {
                if (binding.errorView.visibility != View.GONE) {
                    binding.errorView.visibility = View.GONE
                }
            }
        })

        ingredientViewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                if (binding.progressBar.visibility != View.VISIBLE) {
                    binding.progressBar.visibility = View.VISIBLE
                }
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun findIngredient() {
        ingredientViewModel.fetchIngredients(id!!)
    }

    override fun onDestroy() {
        super.onDestroy()
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