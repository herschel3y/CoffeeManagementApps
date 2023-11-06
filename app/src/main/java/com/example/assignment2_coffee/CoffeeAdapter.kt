package com.example.assignment2_coffee

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment2_coffee.databinding.ItemCoffeeBinding

class CoffeeAdapter(
    private val context: Context,
    private val coffeeList: MutableList<Coffee>,
    private val onEditClick: (Coffee) -> Unit,
    private val onDeleteClick: (Coffee) -> Unit
) : RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder>() {

    inner class CoffeeViewHolder(val binding: ItemCoffeeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coffee: Coffee) {
            binding.descriptionTextView.text = "Coffee Name :  " + coffee.description
            binding.quantityTextView.text = "Coffee Quantity :  "+coffee.quantity.toString()
            binding.priceTextView.text = "Coffee Price :  $"+coffee.price.toString()

            binding.btnEdit.setOnClickListener { onEditClick(coffee) }
            binding.btnDelete.setOnClickListener { onDeleteClick(coffee) }
        }
    }
    fun updateData(updatedCoffeeList: List<Coffee>) {
        this.coffeeList.clear()
        this.coffeeList.addAll(updatedCoffeeList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val binding = ItemCoffeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoffeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val coffee = coffeeList[position]
        holder.bind(coffee)
    }

    override fun getItemCount(): Int = coffeeList.size
}
