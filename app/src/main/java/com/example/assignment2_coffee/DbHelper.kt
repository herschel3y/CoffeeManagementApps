package com.example.assignment2_coffee

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(val context: Context, name: String, version: Int)
    : SQLiteOpenHelper(context, name, null, version) {
    private val createBook =
        "create table $TABLE_NAME (" +
                "$ID_COL integer primary key autoincrement, " +
                "$DESCRIPTION text," +
                "$QUANTITY real," +
                "$PRICE real)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createBook)
    }
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("drop table if exists $TABLE_NAME")
        onCreate(db)
    }
    fun getCoffee(coffeeId: Int): Coffee? {
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM ${DbHelper.TABLE_NAME} WHERE ${DbHelper.ID_COL} = ?", arrayOf(coffeeId.toString()))

        var coffee: Coffee? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(DbHelper.ID_COL))
            val description = cursor.getString(cursor.getColumnIndex(DbHelper.DESCRIPTION))
            val quantity = cursor.getFloat(cursor.getColumnIndex(DbHelper.QUANTITY))
            val price = cursor.getFloat(cursor.getColumnIndex(DbHelper.PRICE))

            coffee = Coffee(id, description, quantity, price)
        }
        cursor.close()

        return coffee
    }
    fun updateCoffee(coffee: Coffee): Boolean {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(DbHelper.DESCRIPTION, coffee.description)
        contentValues.put(DbHelper.QUANTITY, coffee.quantity)
        contentValues.put(DbHelper.PRICE, coffee.price)

        val result = db.update(DbHelper.TABLE_NAME, contentValues, "${DbHelper.ID_COL} = ?", arrayOf(coffee.id.toString()))

        return result != -1
    }

    companion object {
        // below is the variable for table name
        val TABLE_NAME = "Coffee"

        // below is the variable for id column
        val ID_COL = "id"

        // below is the variable for name column
        val DESCRIPTION = "description"

        // below is the variable for matric_no column
        val QUANTITY = "quantity"

        // below is the variable for coursework column
        val PRICE = "price"
    }

}