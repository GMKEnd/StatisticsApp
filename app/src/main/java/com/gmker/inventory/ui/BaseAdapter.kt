package com.gmker.inventory.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmker.inventory.dataBase.Ingredient
import com.gmker.inventory.databinding.RecyclerItemCardBinding

class BaseAdapter(private val testList: List<Ingredient?>) : ListAdapter<Ingredient, BaseAdapter.IngredientViewHolder>(DiffCallback()) {

    class IngredientViewHolder(
        private val binding: RecyclerItemCardBinding,
//        private val onItemClick: (Ingredient) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient) {
            binding.ingredientName.text = ingredient.name
            binding.ingredientAmount.text = ingredient.amount.toString()
            binding.ingredientUnit.text = ingredient.unit
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Ingredient>() {
        override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = RecyclerItemCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun refresh() {
    }
}