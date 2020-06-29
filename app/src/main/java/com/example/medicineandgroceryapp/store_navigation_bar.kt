package com.example.medicineandgroceryapp
import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
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
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayOutputStream

class store_navigation_bar : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    val CAMERA_REQUEST_CODE: Int = 2
    val CAMERA_CODE: Int = 0
    val REQUEST_CODE_FOR_GALLERY: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_navigation_bar)
        val mToolbar =
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_store_profile_items)
        setSupportActionBar(mToolbar)
        val recycleViewOfItemsInCategory =
            findViewById(R.id.recycler_store_profile_items) as RecyclerView
        recycleViewOfItemsInCategory.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val resid: Int = R.id.item_photo_in_store_profile_items;
        val users = ArrayList<DataItemsInStoreProfileAbstract>()
       /* users.add(DataClassForItemsInStoreProfileLabel("abc"))
        users.add(DataClassForItemsInStoreProfile(resid, "Detergent", "300Rs"))
        users.add(DataClassForItemsInStoreProfile(resid, "Detergent", "300Rs"))
        users.add(DataClassForItemsInStoreProfile(resid, "Detergent", "300Rs"))
        users.add(DataClassForItemsInStoreProfile(resid, "Detergent", "300Rs"))*/
        val adapter = CustomAdapterForItemsInStoreProfile(users)
        recycleViewOfItemsInCategory.adapter = adapter
        val floatingButton: FloatingActionButton =
            findViewById(R.id.floatingActionButtonStoreProfile)
        floatingButton.setOnClickListener() { v ->
            getAlertBar()
        }


       /* val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_FOR_GALLERY) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                var intent = Intent(Intent.ACTION_PICK)
                intent.setType("image/*")
                startActivityForResult(Intent.createChooser(intent, "Select Image"), requestCode)

            } else {
                Toast.makeText(applicationContext, "Don't have access", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun getAlertBar() {
        val items: Array<CharSequence> = arrayOf("Camera", "Gallery", "Close")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add picture for new item")
        builder.setItems(items, DialogInterface.OnClickListener { dialog, which ->
            if (items[which].equals("Camera")) {
                Toast.makeText(applicationContext, "Camera", Toast.LENGTH_SHORT).show()
                callCamera()
            } else if (items[which].equals("Gallery")) {
                Toast.makeText(applicationContext, "Gallery", Toast.LENGTH_SHORT).show()
                goIntoGallery()
            } else if (items[which].equals("Close")) {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    fun goIntoGallery() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE_FOR_GALLERY
        )
    }

    private fun callCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startActivityForResult(cameraIntent, CAMERA_CODE)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_CODE) {
            if (data != null) {
                var image = data.extras.get("data") as Bitmap
                val intent = Intent(this, itemsDetailFromCamera::class.java)
                intent.putExtra("image", bitmapToString(image))
                intent.putExtra("type", true)
                startActivity(intent)
            }

        } else if (requestCode == REQUEST_CODE_FOR_GALLERY) {
            if (data != null) {
                val selectedimage: Uri = data.data
                val intent = Intent(this, itemsDetailFromCamera::class.java)
                intent.putExtra("image", selectedimage.toString())
                intent.putExtra("type", false)
                startActivity(intent)
            }
        } else {
            Toast.makeText(applicationContext, requestCode.toString(), Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_items_in_category, menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search_items_in_category)
        val searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchView.isIconified = true
                Toast.makeText(applicationContext, "looking for $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(applicationContext, "looking fotr $newText", Toast.LENGTH_SHORT)
                    .show()
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    fun bitmapToString(bitmap: Bitmap): String {
        var outputStream: ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        var imageBytes = outputStream.toByteArray()
        var encodedImage: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return encodedImage
    }




    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.store_navigation_bar, menu)
        return true
    }*/

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
            R.id.store_drawer_notifications -> {
                // Handle the camera action
            }
            R.id.store_drawer_settings -> {

            }
            R.id.store_drawer_name -> {

            }
            R.id.store_drawer_requests -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
