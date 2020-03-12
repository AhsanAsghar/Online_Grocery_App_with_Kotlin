package com.example.medicineandgroceryapp

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class itemsInStoreProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_in_store_profile)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_store_profile_items)
        setSupportActionBar(mToolbar)
        val recycleViewOfItemsInCategory = findViewById(R.id.recycler_store_profile_items) as RecyclerView
        recycleViewOfItemsInCategory.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        val resid : Int = R.id.item_photo_in_store_profile_items;
        val users = ArrayList<DataItemsInStoreProfileAbstract>()
        users.add(DataClassForItemsInStoreProfileLabel("abc"))
        users.add(DataClassForItemsInStoreProfile(resid,"Detergent", "300Rs"))
        users.add(DataClassForItemsInStoreProfile(resid,"Detergent", "300Rs"))
        users.add(DataClassForItemsInStoreProfile(resid,"Detergent", "300Rs"))
        users.add(DataClassForItemsInStoreProfile(resid,"Detergent", "300Rs"))
        val adapter = CustomAdapterForItemsInStoreProfile(users)
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
                Toast.makeText(applicationContext,"looking for $query", Toast.LENGTH_SHORT).show()
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
