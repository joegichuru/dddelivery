package com.joe.dialdelivery.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joe.dialdelivery.R
import com.joe.network.model.Recipe

class IngredientAdapter(
    private val ingredients: MutableList<Recipe>,
    private val context: Context
) :
    RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(
            LayoutInflater.from(context).inflate(R.layout.ingredient, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val recipe = ingredients[position]
        holder.title.text = recipe.title
        holder.count.text = "${recipe.count}"
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.recipe_image)
        val title: TextView = itemView.findViewById(R.id.title)
        val count: TextView = itemView.findViewById(R.id.available_count)
    }
}