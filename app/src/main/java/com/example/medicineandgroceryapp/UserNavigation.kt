package com.example.medicineandgroceryapp
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserNavigation : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_navigation)


        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar2)
        setSupportActionBar(mToolbar)
        val recycle = findViewById(R.id.recyclerView) as RecyclerView
        val recycleButton = findViewById(R.id.recyclerView) as RecyclerView
        val spinner = findViewById<Spinner>(R.id.spinner)
        val grocormedic = arrayOf("Grocery Store", "Medical Store")
        spinner.adapter = ArrayAdapter<String> (this, android.R.layout.simple_expandable_list_item_1,grocormedic)
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


       }



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




        val toolbar: Toolbar = findViewById(R.id.toolbarItemDetailCamera)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.user_drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.user_drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user_navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.user_notifications-> {
                // Handle the camera action
            }
            R.id.user_settings -> {

            }
            R.id.make_store -> {

            }
            R.id.become_delivery_person -> {

            }
            R.id.user_cart -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.user_drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nearest_stores,menu)
        return super.onCreateOptionsMenu(menu)
    }*/
}
