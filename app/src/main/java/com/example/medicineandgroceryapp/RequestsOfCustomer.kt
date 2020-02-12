package com.example.medicineandgroceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RequestsOfCustomer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests_of_customer)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_requests_of_customers);
        setSupportActionBar(mToolbar)
        val recycleOfCategory = findViewById<RecyclerView>(R.id.recyclerView_requests_of_customers)
        recycleOfCategory.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val resid = R.id.customer_photo
        val users = ArrayList<DataClassForRequestsOfCustomer>()
        users.add(DataClassForRequestsOfCustomer(resid,"Altan"))
        users.add(DataClassForRequestsOfCustomer(resid,"Altan"))
        users.add(DataClassForRequestsOfCustomer(resid,"Altan"))
        users.add(DataClassForRequestsOfCustomer(resid,"Altan"))
        val adapter = CustomAdapterClassForRequestsOfCustomer(users)
        recycleOfCategory.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nearest_stores,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
