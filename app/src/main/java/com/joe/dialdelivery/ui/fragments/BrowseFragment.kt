package com.joe.dialdelivery.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.joe.dialdelivery.R
import com.joe.dialdelivery.databinding.FragmentBrowseBinding
import com.joe.dialdelivery.ui.adapters.IngredientPagerAdapter

class BrowseFragment : Fragment() {
    lateinit var binding: FragmentBrowseBinding
    private  val titles: MutableList<String> = mutableListOf()
    private  val fragments: MutableList<Fragment> =mutableListOf()
    private lateinit var ingredientPagerAdapter: IngredientPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrowseBinding.inflate(inflater, container, false)
        return binding.root
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
        titles.add("Mains")
        fragments.add(IngredientFragment.newInstance(2))
        titles.add("Appetizer")
        fragments.add(IngredientFragment.newInstance(3))
        titles.add("Quick Byte")
        fragments.add(IngredientFragment.newInstance(4))
        titles.add("Bento")
        setUpTabs()
    }

}