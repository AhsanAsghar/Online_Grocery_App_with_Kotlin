package com.example.medicineandgroceryapp

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
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
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search_items_in_category)
        val searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("",false)
                searchView.isIconified = true
                Toast.makeText(applicationContext,"looking for $query",Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(applicationContext,"looking fotr $newText", Toast.LENGTH_SHORT).show()
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}
