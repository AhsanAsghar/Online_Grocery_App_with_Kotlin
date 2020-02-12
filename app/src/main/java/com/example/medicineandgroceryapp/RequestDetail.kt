package com.example.medicineandgroceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RequestDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_detail)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_request_details);
        setSupportActionBar(mToolbar)
        val recycleOfCategory = findViewById<RecyclerView>(R.id.recyclerView_request_detail)
        recycleOfCategory.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val resid = R.id.cart_item_photo_request_detail
        val users = ArrayList<DataClassForRequestDetails>()
        users.add(DataClassForRequestDetails(resid,"Al Habib", "No Request Sent"))
        users.add(DataClassForRequestDetails(resid,"Detergents", "300 Rs"))
        users.add(DataClassForRequestDetails(resid,"Detergents", "300 Rs"))
        users.add(DataClassForRequestDetails(resid,"Detergents", "300 Rs"))
        val adapter = CustomAdapterClassForRequestDetail(users)
        recycleOfCategory.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nearest_stores,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
