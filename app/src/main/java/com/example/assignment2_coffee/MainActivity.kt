package com.example.assignment2_coffee

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment2_coffee.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var adapter: CoffeeAdapter

    companion object {
        const val REQUEST_CODE_UPDATE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        val dbHelper = DbHelper(this, "Coffee.db", 1)

        binding.btnAdd.setOnClickListener {
            intent = Intent(this, AddCoffee::class.java)
            startActivity(intent)
        }
        setupRecyclerView()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_UPDATE && resultCode == Activity.RESULT_OK) {
            refreshRecyclerView()
        }
    }

    private fun setupRecyclerView(){

        val coffeeList = getCoffeeData().toMutableList()

        if(coffeeList.isEmpty()){
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE

        }else {
            binding.tvEmptyState.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            if (!::adapter.isInitialized) {
                adapter = CoffeeAdapter(this, coffeeList,
                    onEditClick = { coffee ->
                        val intent = Intent(this@MainActivity, UpdateCoffee::class.java)
                        intent.putExtra("coffeeId", coffee.id)
                        startActivityForResult(intent, REQUEST_CODE_UPDATE)
                    },
                    onDeleteClick = { coffee ->
                        val intent = Intent(this@MainActivity, DeleteActivity::class.java)
                        intent.putExtra("coffeeId", coffee.id)
                        startActivity(intent)
                    }
                )
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
            }
        }

    }



    private fun refreshRecyclerView() {
        val updatedCoffeeList = getCoffeeData()
        adapter.updateData(updatedCoffeeList)
        setupRecyclerView()
    }



    fun getCoffeeData(): List<Coffee> {
        val db = DbHelper(this, "Coffee.db", 1).readableDatabase
        val coffeeList = mutableListOf<Coffee>()
        val cursor = db.rawQuery("SELECT * FROM ${DbHelper.TABLE_NAME}", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(DbHelper.ID_COL))
                val description = cursor.getString(cursor.getColumnIndex(DbHelper.DESCRIPTION))
                val quantity = cursor.getFloat(cursor.getColumnIndex(DbHelper.QUANTITY))
                val price = cursor.getFloat(cursor.getColumnIndex(DbHelper.PRICE))

                val coffee = Coffee(id, description, quantity, price)
                coffeeList.add(coffee)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return coffeeList
    }
    override fun onResume() {
        super.onResume()
        refreshRecyclerView()
    }
}
