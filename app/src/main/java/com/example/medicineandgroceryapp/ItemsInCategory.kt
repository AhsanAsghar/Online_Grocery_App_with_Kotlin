package com.example.medicineandgroceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class ItemsInCategory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_in_category)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_items_in_category)
        setSupportActionBar(mToolbar)
        val recycleViewOfItemsInCategory = findViewById(R.id.recycler_items_in_category) as RecyclerView
        recycleViewOfItemsInCategory.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        val resid : Int = R.id.item_image;
        val users = ArrayList<DataItemsInCategoryParent>()
        users.add(DataClassForDataItemsInCategoryLabel("Abc"))
        users.add(DataClassForDataItemsInCategory(resid,"Tide","300 Rs"))
        users.add(DataClassForDataItemsInCategory(resid,"Tide","300 Rs"))
        users.add(DataClassForDataItemsInCategoryLabel("Abcd"))
        users.add(DataClassForDataItemsInCategory(resid,"Tide","300 Rs"))
        users.add(DataClassForDataItemsInCategory(resid,"Tide","300 Rs"))
        users.add(DataClassForDataItemsInCategoryLabel("Abcd"))
        users.add(DataClassForDataItemsInCategory(resid,"Tide","300 Rs"))
        val adapter = CustomAdapterForItemsInCategory(users)
        recycleViewOfItemsInCategory.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_items_in_category,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
