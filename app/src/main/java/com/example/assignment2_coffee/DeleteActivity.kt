package com.example.assignment2_coffee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class DeleteActivity : AppCompatActivity() {
    private var coffeeId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        coffeeId = intent.getIntExtra("coffeeId", -1)

        if (coffeeId == -1) {
            finish()
            return
        }

        showDeleteConfirmationDialog()
    }

    private fun showDeleteConfirmationDialog() {
        // Create an alert dialog
        AlertDialog.Builder(this)
            .setTitle("Delete Coffee")
            .setMessage("Are you sure you want to delete this coffee?")
            .setPositiveButton("Yes") { _, _ ->
                deleteCoffee()
            }
            .setNegativeButton("No") { _, _ ->
                finish()
            }
            .show()
    }

    private fun deleteCoffee() {
        val dbHelper = DbHelper(this, "Coffee.db", 1)
        val db = dbHelper.writableDatabase

        val deletedRows = db.delete("Coffee", "id=?", arrayOf(coffeeId.toString()))

        if (deletedRows > 0) {
            Toast.makeText(this, "Coffee deleted successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to delete coffee!", Toast.LENGTH_SHORT).show()
        }
        db.close()
        finish()
    }

}