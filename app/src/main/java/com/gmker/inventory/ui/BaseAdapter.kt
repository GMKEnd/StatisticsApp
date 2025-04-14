package com.gmker.inventory.ui

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmker.inventory.dataBase.Ingredient
import com.gmker.inventory.databinding.RecyclerItemCardBinding
import com.gmker.inventory.util.VarHolder

class BaseAdapter : ListAdapter<Ingredient, BaseAdapter.IngredientViewHolder>(DiffCallback()) {

    private var mHolder: VarHolder? = null

    class IngredientViewHolder(
        private val binding: RecyclerItemCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: Ingredient, onItemClick: OnClickListener) {
            binding.ingredientName.text = ingredient.name
            binding.ingredientAmount.text = ingredient.amount.toString()
            binding.ingredientUnit.text = ingredient.unit
            binding.moreBtn.setOnClickListener(onItemClick)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Ingredient>() {
        override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            val nameUnchanged = oldItem.name == newItem.name
            val amountUnchanged = oldItem.amount == newItem.amount
            val unitUnchanged = oldItem.unit == newItem.unit
            return (nameUnchanged && amountUnchanged && unitUnchanged)
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
        holder.bind(getItem(position)) {
            mHolder?.getBsd()?.setId(getItem(position).id)
            mHolder?.getBsd()?.show()
        }
    }

    fun setHolder(holder: VarHolder) {
        mHolder = holder
    }
}