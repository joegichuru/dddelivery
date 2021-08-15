package com.joe.dialdelivery.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.core.widget.addTextChangedListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.joe.dialdelivery.R
import com.joe.dialdelivery.databinding.FragmentBrowseBinding
import com.joe.dialdelivery.ui.adapters.IngredientAdapter
import com.joe.dialdelivery.ui.adapters.IngredientPagerAdapter
import com.joe.network.ApiClient
import com.joe.network.model.Recipe
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class BrowseFragment : Fragment() {
    lateinit var binding: FragmentBrowseBinding
    private val titles: MutableList<String> = mutableListOf()
    private val fragments: MutableList<Fragment> = mutableListOf()
    private val searchResult: MutableList<Recipe> = mutableListOf()
    private lateinit var adapter: IngredientAdapter
    private lateinit var ingredientPagerAdapter: IngredientPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrowseBinding.inflate(inflater, container, false)
        adapter = IngredientAdapter(searchResult, requireContext())
        binding.ingredientSearchList.adapter = adapter
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.contentView.visibility = View.VISIBLE
                    binding.ingredientSearchList.visibility = View.GONE
                } else {
                    binding.contentView.visibility = View.GONE
                    binding.ingredientSearchList.visibility = View.VISIBLE
                    search(s.toString())
                }
            }

        })

        return binding.root
    }

    private fun search(query: String) {
        ApiClient.provideApiClient(requireContext()).searchIngredients(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                searchResult.clear()
                searchResult.addAll(it.data)
            }, {
                it.printStackTrace()
            }, {
                adapter.notifyDataSetChanged()
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findIngredients()
    }

    private fun setUpTabs() {
        ingredientPagerAdapter = IngredientPagerAdapter(fragments, this)
        binding.viewPager.adapter = ingredientPagerAdapter
        TabLayoutMediator(binding.tabBar, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    private fun findIngredients() {
        fragments.add(IngredientFragment.newInstance(1))
        titles.add("Breakfast")
        fragments.add(IngredientFragment.newInstance(2))
        titles.add("Lunch")
        fragments.add(IngredientFragment.newInstance(3))
        titles.add("Dinner")
        fragments.add(IngredientFragment.newInstance(4))
        titles.add("Snacks")
        setUpTabs()
    }

}