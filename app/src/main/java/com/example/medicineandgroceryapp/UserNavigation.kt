package com.example.medicineandgroceryapp
import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
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
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import pub.devrel.easypermissions.EasyPermissions
import kotlinx.android.synthetic.main.activity_biodata_of_store.*

class UserNavigation : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val RC_LOCATION_PERM = 124
    var phone : String? = null
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val PERMISSION_ID = 42
    var latitude : String = ""
    var longitude : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_navigation)
        if (intent.getStringExtra("phone") != null) {
            phone = intent.getStringExtra("phone")
        } else {
            phone = "+923450694449"
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationTask()


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
                   val queu = Volley.newRequestQueue(applicationContext)
                   var url: String =
                       "https://grocerymedicineapp.000webhostapp.com/PHPfiles/NearestStoresFinding.php"
                   val postRequest =
                       object : StringRequest(Request.Method.POST, url, Response.Listener { response ->
                           Log.d("response", response.toString())
                           Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT)
                               .show()
                       }, Response.ErrorListener { error ->
                           Log.d("error", error.toString())
                           Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT)
                               .show()
                       }) {
                           override fun getParams(): Map<String, String> {
                               val params = HashMap<String, String>()
                               params.put("store_type", "Grocery Store")
                               params.put("source_latitude",latitude)
                               params.put("source_longitude",longitude)
                               return params
                           }
                       }
                   queu.add(postRequest)
               }

               else if(position == 1){
                   Toast.makeText(applicationContext , "Medical", Toast.LENGTH_SHORT).show()
                   val queu = Volley.newRequestQueue(applicationContext)
                   var url: String =
                       "https://grocerymedicineapp.000webhostapp.com/PHPfiles/NearestStoresFinding.php"
                   val postRequest =
                       object : StringRequest(Request.Method.POST, url, Response.Listener { response ->
                           Log.d("response", response.toString())
                           Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT)
                               .show()
                       }, Response.ErrorListener { error ->
                           Log.d("error", error.toString())
                           Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT)
                               .show()
                       }) {
                           override fun getParams(): Map<String, String> {
                               val params = HashMap<String, String>()
                               params.put("store_type", "Medical Store")
                               params.put("source_latitude",latitude)
                               params.put("source_longitude",longitude)
                               return params
                           }
                       }
                   queu.add(postRequest)
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
        //get WHO IS USER
        val queue = Volley.newRequestQueue(this)
        val url_get_hiring_status : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/whoIsUser.php?phone=$phone"
        val request_hiring_status : StringRequest = StringRequest(url_get_hiring_status, Response.Listener {
                response ->
            var statusOfUser  = response.toString().split(":").toTypedArray()
            val whoIsUser = statusOfUser[1].substring(1,statusOfUser[1].length - 2)
            Log.d("WHO", whoIsUser)
            if(whoIsUser.equals("SO")){
                Log.d("WHOin", "in")
                navView.menu.clear()
                navView.inflateMenu(R.menu.activity_store_navigation_bar_drawer)
            }else if (whoIsUser == "DP"){
                Log.d("WHOin", "in")
                navView.menu.clear()
                navView.inflateMenu(R.menu.activity_deliveryperson_navigation_drawer)
            }
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@UserNavigation,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request_hiring_status))
        //End getting WHO IS USER

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        Toast.makeText(this, "Location given !", Toast.LENGTH_LONG).show()
    }
    fun locationTask() {
        if (hasLocationPermissions())
        {
            // Have permission, do the thing!
            Toast.makeText(this, "TODO: Location things", Toast.LENGTH_LONG).show()
            mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                var location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    latitude = location?.latitude.toString()
                    longitude = location?.longitude.toString()
                    findViewById<TextView>(R.id.currentaddress).text = location?.longitude.toString() +"," + location?.latitude.toString()
                    val queu = Volley.newRequestQueue(applicationContext)
                    var url: String =
                        "https://grocerymedicineapp.000webhostapp.com/PHPfiles/InsertAddressOfCustomer.php"
                    val postRequest =
                        object : StringRequest(Request.Method.POST, url, Response.Listener { response ->
                            Log.d("response", response.toString())
                            Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }, Response.ErrorListener { error ->
                            Log.d("error", error.toString())
                            Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }) {
                            override fun getParams(): Map<String, String> {
                                val params = HashMap<String, String>()
                                params.put("phone", phone.toString())
                                params.put("latitude",latitude)
                                params.put("longitude",longitude)
                                return params
                            }
                        }
                    queu.add(postRequest)
                    //store_address =  location?.longitude.toString() +"," + location?.latitude.toString()
                }
            }
        }
        else
        {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                this,
                "G allow krso k mood ney !",
                RC_LOCATION_PERM,
                Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            if(mLastLocation != null){
                findViewById<TextView>(R.id.currentaddress).text = mLastLocation.longitude.toString() +"," + mLastLocation.latitude.toString()

                val queu = Volley.newRequestQueue(applicationContext)
                var url: String =
                    "https://grocerymedicineapp.000webhostapp.com/PHPfiles/InsertAddressOfCustomer.php"
                val postRequest =
                    object : StringRequest(Request.Method.POST, url, Response.Listener { response ->
                        Log.d("response", response.toString())
                        Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }, Response.ErrorListener { error ->
                        Log.d("error", error.toString())
                        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }) {
                        override fun getParams(): Map<String, String> {
                            val params = HashMap<String, String>()
                            params.put("phone", phone.toString())
                            params.put("latitude",latitude)
                            params.put("longitude",longitude)
                            return params
                        }
                    }
                queu.add(postRequest)
                //store_address =  mLastLocation.longitude.toString() +"," + mLastLocation.latitude.toString()
            }

        }
    }


    private fun hasLocationPermissions():Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)
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
                Toast.makeText(this@UserNavigation,"User Notification",Toast.LENGTH_SHORT).show()
            }
            R.id.user_settings -> {
                Toast.makeText(this@UserNavigation,"User Setting",Toast.LENGTH_SHORT).show()
            }
            R.id.make_store -> {
                Toast.makeText(this@UserNavigation,"Make Store",Toast.LENGTH_SHORT).show()
            }
            R.id.become_delivery_person -> {
                Toast.makeText(this@UserNavigation,"Become a delivery person",Toast.LENGTH_SHORT).show()
            }
            R.id.user_cart -> {
                Toast.makeText(this@UserNavigation,"User Cart",Toast.LENGTH_SHORT).show()
            }
            R.id.store_drawer_notifications -> {
                Toast.makeText(this@UserNavigation,"Store drawer notification",Toast.LENGTH_SHORT).show()
            }
            R.id.store_drawer_name -> {
                Toast.makeText(this@UserNavigation,"Store drawer name",Toast.LENGTH_SHORT).show()
            }
            R.id.store_drawer_requests -> {
                Toast.makeText(this@UserNavigation,"Store drawer request",Toast.LENGTH_SHORT).show()
            }
            R.id.store_drawer_settings -> {
                Toast.makeText(this@UserNavigation,"Store drawer settings",Toast.LENGTH_SHORT).show()
            }
            R.id.deliveryperson_drawer_notifications -> {
                Toast.makeText(this@UserNavigation,"Delivery person drawer notification",Toast.LENGTH_SHORT).show()
            }
            R.id.deliveryperson_drawer_profile  -> {
                Toast.makeText(this@UserNavigation,"Delivery person drawer profile",Toast.LENGTH_SHORT).show()
            }
            R.id.deliveryperson_drawer_requests -> {
                Toast.makeText(this@UserNavigation,"Delivery person drawer requests",Toast.LENGTH_SHORT).show()
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
