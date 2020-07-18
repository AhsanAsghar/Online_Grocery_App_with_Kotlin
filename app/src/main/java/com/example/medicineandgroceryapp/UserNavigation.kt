package com.example.medicineandgroceryapp
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import pub.devrel.easypermissions.EasyPermissions
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
        val recycle = findViewById(R.id.recyclerView) as RecyclerView
        recycle.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        phone = intent.getStringExtra("phone")
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationTask()
        val users = ArrayList<DataClassForNearbyStores> ()
        val navigation : NavigationView = findViewById(R.id.nav_view)
        val view : View = navigation.getHeaderView(0)
        val nav_profile_photo: de.hdodenhof.circleimageview.CircleImageView =
            view.findViewById(R.id.user_Navigation_profile_image)
        val name = view.findViewById<TextView>(R.id.name_of_user_text)
        //Get image
        val queue1 = Volley.newRequestQueue(this)
        val url_img = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/settings_img.php?phone=$phone"
        val request_img : ImageRequest = ImageRequest(url_img, Response.Listener {
                response ->
            nav_profile_photo.setImageBitmap(response)

        },0,0,null,Response.ErrorListener {
                error ->
            Log.d("photo",error.toString())

        } )
        queue1.add(request_img)
        //End getting Image
        //Get name
        val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/settings_get.php?phone=$phone"
        val request : StringRequest = StringRequest(url_get,Response.Listener {
                response ->
            val jObject : JSONObject = JSONObject(response.toString())
            val jsonArray :JSONArray= jObject?.getJSONArray("response")!!
            val jsonObject : JSONObject = jsonArray.getJSONObject(0);
            val namejson : String = jsonObject.getString("name")
            name.setText(namejson)
        },Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@UserNavigation,error.toString(),Toast.LENGTH_SHORT).show()
        })
        queue1.add((request))
        //End Getting name
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar2)
        setSupportActionBar(mToolbar)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val grocormedic = arrayOf("Please Select Store Category","Grocery Store", "Medical Store")
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
               if(position == 1){
                   val store_type = "Grocery+Store"
                   Toast.makeText(applicationContext , "Grocery", Toast.LENGTH_SHORT).show()
                   val queue = Volley.newRequestQueue(applicationContext)
                   val url_get1 : String =
                       "https://grocerymedicineapp.000webhostapp.com/PHPfiles/NearestStoresFinding.php?store_type=$store_type&source_latitude=$latitude&source_longitude=$longitude"
                   val request1 : StringRequest = StringRequest(url_get1, Response.Listener {
                           response ->
                       Log.d("json",response.toString())
                       val jObject : JSONObject = JSONObject(response.toString())
                       val jsonArray : JSONArray = jObject?.getJSONArray("response")!!
                       Log.d("json",jsonArray.toString())
                       val a :Int = jsonArray.length()
                       Log.d("json",a.toString())
                       for(y in 1..a-1){
                           Log.d("Nearest", "Stores")
                           val store_id = jsonArray.getJSONObject(y).getString("store_id")
                           val pimageString = jsonArray.getJSONObject(y).getString("store_image")
                           val store_image = stringToBitmap(pimageString)
                           val store_name = jsonArray.getJSONObject(y).getString("store_name")
                           val distance = jsonArray.getJSONObject(y).getString("distance")
                           val distance1 = distance.toDouble()
                           val distance2 = "%.2f".format(distance1)
                           if(phone != null){
                               users.add(DataClassForNearbyStores(store_image,store_name, distance2+"KM",
                                   phone!!, this@UserNavigation,store_id))
                           }

                       }
                       val adapter = CustomAdapterForNearbyStores(users)
                       recycle.adapter = adapter
                   }, Response.ErrorListener {
                           error ->
                       Log.d("json", error.toString())
                       Toast.makeText(this@UserNavigation,error.toString(), Toast.LENGTH_SHORT).show()
                   })
                   queue.add((request1))
               }

               else if(position == 3){
                   Toast.makeText(applicationContext , "Medical", Toast.LENGTH_SHORT).show()
                   val store_type = "Medical+Store"
                   val queue = Volley.newRequestQueue(applicationContext)
                   val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/NearestStoresFinding.php?store_type=$store_type&source_latitude=$latitude&source_longitude=$longitude"
                   val request : StringRequest = StringRequest(url_get, Response.Listener {
                           response ->
                       Log.d("json",response.toString())
                       val jObject : JSONObject = JSONObject(response.toString())
                       val jsonArray : JSONArray = jObject?.getJSONArray("response")!!
                       Log.d("json",jsonArray.toString())
                       val a = jsonArray.length()
                       Log.d("json",a.toString())
                       for(y in 1..a-1){
                           Log.d("Nearest", "Stores")
                           val store_id = jsonArray.getJSONObject(y).getString("store_id")
                           val pimageString = jsonArray.getJSONObject(y).getString("store_image")
                           val store_image = stringToBitmap(pimageString)
                           val store_name = jsonArray.getJSONObject(y).getString("store_name")
                           val distance = jsonArray.getJSONObject(y).getString("distance")
                          // val distance1 = distance.toDouble()
                           //val distance2 = Math.round(distance1 * 100.0) / 100.0
                           //val distance1 = DecimalFormat("#.###")
                           //distance1.roundingMode = RoundingMode.CEILING
                           //val distance2 = distance1.format(distance)
                           val distance1 = distance.toDouble()
                           val distance2 = "%.2f".format(distance1)
                           Toast.makeText(this@UserNavigation,"Distance:"+distance2,Toast.LENGTH_LONG).show()
                           if(phone != null)
                           users.add(DataClassForNearbyStores(store_image,store_name, distance2.toString() + "KM",phone!!,this@UserNavigation,store_id))
                       }
                       val adapter = CustomAdapterForNearbyStores(users)
                       recycle.adapter = adapter
                   }, Response.ErrorListener {
                           error ->
                       Log.d("json", error.toString())
                       Toast.makeText(this@UserNavigation,error.toString(), Toast.LENGTH_SHORT).show()
                   })
                   queue.add((request))
               }

           }

       }

        val toolbar: Toolbar = findViewById(R.id.toolbarItemDetailCamera)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        //Code to change or copy
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
            val statusOfUser  = response.toString().split(":").toTypedArray()
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
            }else if(whoIsUser.equals("C")){

            }
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@UserNavigation,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request_hiring_status))
        //End getting WHO IS USER
        //End code to change or copy

    }

    override fun onRestart() {
        super.onRestart()
        this@UserNavigation.finish()
        startActivity(intent)
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

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
    }*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        //Code to change or copy
        when (item.itemId) {
            R.id.user_notifications-> {
                // Handle the camera action
                val intent = Intent(this@UserNavigation,Notifications::class.java)
                intent.putExtra("phone",phone)
                startActivity(intent)
                Toast.makeText(this@UserNavigation,"User Notification",Toast.LENGTH_SHORT).show()
            }
            R.id.user_settings -> {
                val intent = Intent(this@UserNavigation,settings::class.java)
                intent.putExtra("phone",phone)
                startActivity(intent)
                Toast.makeText(this@UserNavigation,"User Setting",Toast.LENGTH_SHORT).show()
            }
            R.id.make_store -> {
                val intent = Intent(this@UserNavigation,biodata_of_store::class.java)
                intent.putExtra("phone",phone)
                startActivity(intent)
                Toast.makeText(this@UserNavigation,"Make Store",Toast.LENGTH_SHORT).show()
            }
            R.id.become_delivery_person -> {
                val intent = Intent(this@UserNavigation,biodata_of_deliveryperson::class.java)
                intent.putExtra("phone",phone)
                startActivity(intent)
                Toast.makeText(this@UserNavigation,"Become a delivery person",Toast.LENGTH_SHORT).show()
            }
            R.id.user_cart -> {
                val intent = Intent(this@UserNavigation,StoreNameInCart::class.java)
                intent.putExtra("phone",phone)
                startActivity(intent)
                Toast.makeText(this@UserNavigation,"User Cart",Toast.LENGTH_SHORT).show()
            }
            R.id.store_drawer_notifications -> {
                val intent = Intent(this@UserNavigation,Notifications::class.java)
                intent.putExtra("phone",phone)
                startActivity(intent)
                Toast.makeText(this@UserNavigation,"Store drawer notification",Toast.LENGTH_SHORT).show()
            }
            R.id.store_drawer_name -> {
                val intent = Intent(this@UserNavigation,itemsInStoreProfile::class.java)
                intent.putExtra("phone",phone)
                startActivity(intent)
                Toast.makeText(this@UserNavigation,"Store drawer name",Toast.LENGTH_SHORT).show()
            }
            R.id.store_drawer_requests -> {
                val intent =  Intent(this@UserNavigation,RequestsOfCustomer::class.java)
                intent.putExtra("phone",phone)
                startActivity(intent)
                Toast.makeText(this@UserNavigation,"Store drawer request",Toast.LENGTH_SHORT).show()
            }
            R.id.store_drawer_cart -> {
                val intent = Intent(this@UserNavigation,StoreNameInCart::class.java)
                intent.putExtra("phone",phone)
                startActivity(intent)
                Toast.makeText(this@UserNavigation,"Store Cart",Toast.LENGTH_SHORT).show()
            }
            R.id.deliveryperson_drawer_notifications -> {
                val intent = Intent(this@UserNavigation,Notifications::class.java)
                intent.putExtra("phone",phone)
                startActivity(intent)
                Toast.makeText(this@UserNavigation,"Delivery person drawer notification",Toast.LENGTH_SHORT).show()
            }
            R.id.deliveryperson_drawer_profile  -> {
                val intent = Intent(this@UserNavigation,delivery_person_profile::class.java)
                intent.putExtra("phone",phone)
                startActivity(intent)
                Toast.makeText(this@UserNavigation,"Delivery person drawer profile",Toast.LENGTH_SHORT).show()
            }
            R.id.deliveryperson_drawer_requests -> {
                Toast.makeText(this@UserNavigation,"Delivery person drawer requests",Toast.LENGTH_SHORT).show()
            }
            R.id.deliveryperson_drawer_cart ->{
                val intent = Intent(this@UserNavigation,StoreNameInCart::class.java)
                intent.putExtra("phone",phone)
                startActivity(intent)
                Toast.makeText(this@UserNavigation,"Dilivery person Cart",Toast.LENGTH_SHORT).show()
            }
            R.id.user_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@UserNavigation,MainActivity::class.java)
                startActivity(intent)
            }
            R.id.store_onwer_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@UserNavigation,MainActivity::class.java)
                startActivity(intent)
            }
            R.id.delivery_person_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@UserNavigation,MainActivity::class.java)
                startActivity(intent)
            }
        }
        //end code to change or copy
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
