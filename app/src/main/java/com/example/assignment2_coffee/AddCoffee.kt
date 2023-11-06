package com.example.assignment2_coffee

import android.app.Activity
import android.content.ContentValues
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.assignment2_coffee.databinding.ActivityAddCoffeeBinding

class AddCoffee : AppCompatActivity() {
    private lateinit var binding:ActivityAddCoffeeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCoffeeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        val dbHelper = DbHelper(this, "Coffee.db", 1)
        binding.btnClear.setOnClickListener {
            binding.editDescription.text.clear()
            binding.editQuantity.text.clear()
            binding.editPrice.text.clear()
        }

        binding.btnAdd.setOnClickListener {
            var description = binding.editDescription.text.toString().trim()
            var quantity = binding.editQuantity.text.toString().trim()
            var price = binding.editPrice.text.toString().trim()
            val db = dbHelper.writableDatabase
            try {
                if (description.isNotEmpty() && quantity.isNotEmpty() && price.isNotEmpty()) {
                    val values = ContentValues().apply {
                        put(DESCRIPTION, description)
                        put(QUANTITY, quantity)
                        put(PRICE, price)
                    }
                    db.insertOrThrow(TABLE_NAME, null, values)

                    binding.editDescription.text.clear()
                    binding.editQuantity.text.clear()
                    binding.editPrice.text.clear()

                    Toast.makeText(
                        this, R.string.successful,
                        Toast.LENGTH_SHORT
                    ).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(
                        this, R.string.unsuccessful,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: SQLiteException) {
                Log.e("Database Error", e.message ?: "Unknown error")
                Toast.makeText(
                    this, "Database error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                db.close()
            }
        }


    }
    companion object {
        private val DATABASE_NAME = "CoffeeData.db"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "Coffee"
        val ID_COL = "id"
        val DESCRIPTION = "description"
        val QUANTITY = "quantity"
        val PRICE = "price"
    }

}


