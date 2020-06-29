package com.example.medicineandgroceryapp
import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Base64
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
import org.json.JSONArray
import org.json.JSONObject

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

        val users = ArrayList<DataClassForNearbyStores> ()

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
                   val store_type = "Grocery Store"
                   Toast.makeText(applicationContext , "Grocery", Toast.LENGTH_SHORT).show()
                   val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/NearestStoreFinding.php?store_type=$store_type&source_latitude=$latitude&source_longitude=$longitude"
                   val request : StringRequest = StringRequest(url_get, Response.Listener {
                           response ->
                       Log.d("json",response.toString())
                       val jObject : JSONObject = JSONObject(response.toString())
                       val jsonArray : JSONArray = jObject?.getJSONArray("response")!!
                       Log.d("json",jsonArray.toString())
                       val a = jsonArray.length()
                       Log.d("json",a.toString())
                       for(y in 0..a-1){
                           Log.d("Nearest", "Stores")
                           val product_id = jsonArray.getJSONObject(y).getString("product_id")
                           val product_name = jsonArray.getJSONObject(y).getString("product_name")
                           val pimageString = jsonArray.getJSONObject(y).getString("product_image")
                           val product_img = stringToBitmap(pimageString)
                           val productPrice = jsonArray.getJSONObject(y).getString("product_price")
                           //users.add(DataClassForNearbyStores(product_img,product_name, productPrice))
                       }
                       //val adapter = CustomAdapterClassForRequestDetail(users)
                       //recycleOfCategory.adapter = adapter
                   }, Response.ErrorListener {
                           error ->
                       Log.d("json", error.toString())
                      // Toast.makeText(this@RequestDetail,error.toString(), Toast.LENGTH_SHORT).show()
                   })
                   //queue.add((request))
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
    fun stringToBitmap(imageInString : String) : Bitmap {
        val imageBytes = Base64.decode(imageInString,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return image
    }
    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nearest_stores,menu)
        return super.onCreateOptionsMenu(menu)
    }*/
}
