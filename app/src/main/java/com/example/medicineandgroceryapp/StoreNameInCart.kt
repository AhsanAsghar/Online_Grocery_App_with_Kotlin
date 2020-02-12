package com.example.medicineandgroceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StoreNameInCart : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_name_in_cart)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_store_name_in_cart);
        setSupportActionBar(mToolbar)
        val recycleOfCategory = findViewById<RecyclerView>(R.id.recyclerView_store_name_in_cart)
        recycleOfCategory.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val resid = R.id.category_photo
        val users = ArrayList<DataClassStoreNameInCart>()
        users.add(DataClassStoreNameInCart(resid,"Al Habib"))
        users.add(DataClassStoreNameInCart(resid,"Usama Bakers"))
        users.add(DataClassStoreNameInCart(resid,"Al Jannat"))
        users.add(DataClassStoreNameInCart(resid,"Al Kuwair"))
        val adapter = CustomAdapterClassForStoreNameInCart(users)
        recycleOfCategory.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nearest_stores,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
