package com.example.medicineandgroceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Notifications : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_notification);
        setSupportActionBar(mToolbar)
        val recycleOfCategory = findViewById<RecyclerView>(R.id.recyclerView_notification)
        recycleOfCategory.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val resid = R.id.notification_photo
        val users = ArrayList<DataClassForNotifications>()
        users.add(DataClassForNotifications(resid,"Detergents"))
        users.add(DataClassForNotifications(resid,"Detergents"))
        users.add(DataClassForNotifications(resid,"Detergents"))
        users.add(DataClassForNotifications(resid,"Detergents"))
        val adapter = CustomAdapterClassForNotifications(users)
        recycleOfCategory.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nearest_stores,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
