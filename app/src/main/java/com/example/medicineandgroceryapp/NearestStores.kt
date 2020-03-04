package com.example.medicineandgroceryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NearestStores : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearest_stores)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar2)
        setSupportActionBar(mToolbar)
        val recycle = findViewById(R.id.recyclerView) as RecyclerView
        val recycleButton = findViewById(R.id.recyclerViewStoreSelection) as RecyclerView
        val spinner = findViewById<Spinner>(R.id.spinner)
        val grocormedic = arrayOf("Grocery Store", "Medical Store")
        /*spinner.adapter = ArrayAdapter<String> (this, android.R.layout.simple_expandable_list_item_1,grocormedic)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position == 0){
                    Toast.makeText(applicationContext , "Grocery", Toast.LENGTH_SHORT).show()
                }
                else if(position == 1){
                    Toast.makeText(applicationContext , "Medical", Toast.LENGTH_SHORT).show()
                }
            }


        }*/
        recycle.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        recycleButton.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        val storeCategory = ArrayList<DataClassStoreCategoryButton>()
        storeCategory.add(DataClassStoreCategoryButton("Grocery Store"))
        storeCategory.add(DataClassStoreCategoryButton("Bakery Store"))
        storeCategory.add(DataClassStoreCategoryButton("Pharmacy"))
        storeCategory.add(DataClassStoreCategoryButton("General Store"))
        val buttonAdapter = CustomDataStoreCategoryButton(storeCategory)
        recycleButton.adapter = buttonAdapter
        val users = ArrayList<DataClassForNearbyStores> ()
        val resid = R.drawable.store
        users.add(DataClassForNearbyStores(resid,"Store name","3 km"))
        users.add(DataClassForNearbyStores(resid,"Store name","3 km"))
        users.add(DataClassForNearbyStores(resid,"Store name","3 km"))
        users.add(DataClassForNearbyStores(resid,"Store name","3 km"))

        val adapter = CustomAdapterForNearbyStores(users)
        recycle.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nearest_stores,menu)
        return super.onCreateOptionsMenu(menu)
    }
}


