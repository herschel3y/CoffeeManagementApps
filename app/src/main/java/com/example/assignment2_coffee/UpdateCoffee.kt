package com.example.assignment2_coffee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.widget.Toast
import com.example.assignment2_coffee.databinding.ActivityUpdateCoffeeBinding

class UpdateCoffee : AppCompatActivity() {
    private lateinit var binding:ActivityUpdateCoffeeBinding
    private var coffeeId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateCoffeeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        // Get coffeeId from intent
        coffeeId = intent.getIntExtra("coffeeId", -1)

        if (coffeeId == -1) {
            finish()
            return
        }

        loadCoffeeData()

        binding.btnUpdate.setOnClickListener {
            updateCoffeeData()
        }
    }
    private fun loadCoffeeData() {
        val dbHelper = DbHelper(this, "Coffee.db", 1)
        val coffee = dbHelper.getCoffee(coffeeId)

        if (coffee != null) {
            binding.editDescription.setText(coffee.description)
        }
        if (coffee != null) {
            binding.editQuantity.setText(coffee.quantity.toString())
        }
        if (coffee != null) {
            binding.editPrice.setText(coffee.price.toString())
        }
    }
    private fun updateCoffeeData() {
        val newDescription = binding.editDescription.text.toString()
        val newQuantity = binding.editQuantity.text.toString().toFloatOrNull()
        val newPrice = binding.editPrice.text.toString().toFloatOrNull()

        if (newQuantity == null || newPrice == null) {
            Toast.makeText(
                this, com.example.assignment2_coffee.R.string.unsuccessful,
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val updatedCoffee = Coffee(coffeeId, newDescription, newQuantity, newPrice)
        val dbHelper = DbHelper(this, "Coffee.db", 1)
        dbHelper.updateCoffee(updatedCoffee)
        setResult(Activity.RESULT_OK)
        finish()

    }
}
