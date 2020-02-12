package com.example.medicineandgroceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class cart_items : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_items)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_cart_items);
        setSupportActionBar(mToolbar)
        val recycleOfCategory = findViewById<RecyclerView>(R.id.recyclerView_cart_items)
        recycleOfCategory.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val resid = R.id.cart_item_photo
        val users = ArrayList<DataClassForCartItems>()
        users.add(DataClassForCartItems(resid,"Al Habib", "No Request Sent"))
        users.add(DataClassForCartItems(resid,"Detergents", "300 Rs"))
        users.add(DataClassForCartItems(resid,"Detergents", "300 Rs"))
        users.add(DataClassForCartItems(resid,"Detergents", "300 Rs"))
        val adapter = CustomAdapterClassForCartItems(users)
        recycleOfCategory.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nearest_stores,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
