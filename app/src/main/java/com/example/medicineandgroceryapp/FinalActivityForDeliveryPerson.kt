package com.example.medicineandgroceryapp

import android.Manifest
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

class FinalActivityForDeliveryPerson : AppCompatActivity() {
    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap: GoogleMap
    var phone:String? = null
    var store_latitude:String = ""
    var store_longitude:String = ""
    var customer_latitude:String = ""
    var customer_longitude:String = ""
    private val RC_LOCATION_PERM = 124
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    internal lateinit var mLocationRequest: LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_for_delivery_person)
        mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_delivery_person_final_activity) as SupportMapFragment
        if (intent.getStringExtra("phone") != null ) {
            phone = intent.getStringExtra("phone")
        } else {
            phone = "+923004579023"
        }


        /*Handler().postDelayed({

            locationTask()
        }, 6000)*/


        val handler: Handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                mLocationRequest = LocationRequest()
                //locationTask()
                val queue = Volley.newRequestQueue(applicationContext)
                val url_get: String =
                    "https://grocerymedicineapp.000webhostapp.com/PHPfiles/TrackDpForDp.php?phone=$phone"
                val request: StringRequest = StringRequest(url_get, Response.Listener { response ->
                    Log.d("json", response.toString())
                    //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
                    //var json : JSONArray = response.getJSONArray(0)
                    val jObject: JSONObject = JSONObject(response.toString())
                    val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
                    val jsonObject: JSONObject = jsonArray.getJSONObject(0);
                    val dp_phonejson: String = jsonObject.getString("phone_number")
                    //val dp_namejson: String = jsonObject.getString("dp_name")
                    val dp_latitudejson: String = jsonObject.getString("latitude")
                    val dp_longitudejson: String = jsonObject.getString("longitude")



                    if(dp_latitudejson != "null" && dp_longitudejson!= "null"){
                        var location = LatLng(dp_latitudejson.toDouble(),dp_longitudejson.toDouble())



                        mapFragment.getMapAsync(OnMapReadyCallback {
                            googleMap = it
                            var marker: Marker? = null
                            if (marker == null) {
                                val options = MarkerOptions().position(location)
                                    .title("Delivery Person")
                                marker = googleMap.addMarker(options)
                            } else {
                                marker!!.setPosition(location)
                            }
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(16f))
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,18f))

                            Toast.makeText(applicationContext,"New Marker Updated",Toast.LENGTH_LONG).show()
                            Log.d("Location", location.toString())
                        })
                    }

                }, Response.ErrorListener { error ->
                    Log.d("json", error.toString())
                    Toast.makeText(this@FinalActivityForDeliveryPerson, error.toString(), Toast.LENGTH_SHORT)
                        .show()
                })
                queue.add((request))
                handler.postDelayed(this,3000)
            }
        }, 3000)
        gettingStoreAddress()
        // gettingCustomerAddress()

    }


    fun gettingStoreAddress(){
        val store_location =  findViewById<EditText>(R.id.StoreLocation)
        val queue = Volley.newRequestQueue(applicationContext)
        val url_get: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/gettingStoreAddressForDp.php?phone=$phone"
        var request: StringRequest = StringRequest(url_get, Response.Listener { response ->
            Log.d("json", response.toString())
            //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
            //var json : JSONArray = response.getJSONArray(0)
            val jObject: JSONObject = JSONObject(response.toString())
            val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
            val jsonObject: JSONObject = jsonArray.getJSONObject(0);
            store_latitude = jsonObject.getString("latitude")
            store_longitude = jsonObject.getString("longitude")

            //Toast.makeText(this@MapForHiringDeliveryPerson, "store id:"+store_id2, Toast.LENGTH_LONG).show()


            val location2 = LatLng(store_latitude.toDouble(),store_longitude.toDouble())
            //Map Marker Working
            mapFragment.getMapAsync(OnMapReadyCallback {
                googleMap = it
                createMarker(store_latitude.toDouble(), store_longitude.toDouble(), "Store Location", "")
               // googleMap.moveCamera(CameraUpdateFactory.newLatLng(location2))
                //googleMap.animateCamera(CameraUpdateFactory.zoomTo(16f))
                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location2,13f))
                Toast.makeText(this@FinalActivityForDeliveryPerson, "Marker Updated", Toast.LENGTH_SHORT).show()

            })


            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(this, Locale.getDefault())
            addresses = geocoder.getFromLocation(store_latitude.toDouble(),store_longitude.toDouble(),1)
            val address:String= addresses.get(0).getAddressLine(0)
            val city:String = addresses.get(0).locality
            val state: String  = addresses.get(0).getAdminArea();
            val country: String = addresses.get(0).getCountryName();

            store_location.setText(address+","+city)



        }, Response.ErrorListener { error ->
            Log.d("json", error.toString())
            Toast.makeText(this@FinalActivityForDeliveryPerson, error.toString(), Toast.LENGTH_SHORT)
                .show()
        })
        queue.add((request))
    }
    fun gettingCustomerAddress(){
        val customer_location = findViewById<EditText>(R.id.CustomerLocation)
        val queue = Volley.newRequestQueue(applicationContext)
        val url_get: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/gettingustomerAddressForDp.php?phone=$phone"
        var request: StringRequest = StringRequest(url_get, Response.Listener { response ->
            Log.d("json", response.toString())
            //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
            //var json : JSONArray = response.getJSONArray(0)
            val jObject: JSONObject = JSONObject(response.toString())
            val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
            val jsonObject: JSONObject = jsonArray.getJSONObject(0);
            customer_latitude = jsonObject.getString("latitude")
            customer_longitude = jsonObject.getString("longitude")

            //Toast.makeText(this@MapForHiringDeliveryPerson, "store id:"+store_id2, Toast.LENGTH_LONG).show()


            val location2 = LatLng(customer_latitude.toDouble(),customer_longitude.toDouble())
            //Map Marker Working
            mapFragment.getMapAsync(OnMapReadyCallback {
                googleMap = it
                createMarker(customer_latitude.toDouble(), customer_longitude.toDouble(), "Customer Location", "")
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location2))
                //googleMap.animateCamera(CameraUpdateFactory.zoomTo(16f))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location2,13f))
                Toast.makeText(this@FinalActivityForDeliveryPerson, "Marker Updated", Toast.LENGTH_SHORT).show()

            })


            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(this, Locale.getDefault())
            addresses = geocoder.getFromLocation(customer_latitude.toDouble(),customer_longitude.toDouble(),1)
            val address:String= addresses.get(0).getAddressLine(0)
            val city:String = addresses.get(0).locality
            val state: String  = addresses.get(0).getAdminArea();
            val country: String = addresses.get(0).getCountryName();

            customer_location.setText(address+","+city)



        }, Response.ErrorListener { error ->
            Log.d("json", error.toString())
            Toast.makeText(this@FinalActivityForDeliveryPerson, error.toString(), Toast.LENGTH_SHORT)
                .show()
        })
        queue.add((request))
    }
    fun TrackDp(){

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        Toast.makeText(this, "Location given !", Toast.LENGTH_LONG).show()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    fun locationTask() {
        if (hasLocationPermissions())
        {
            // Have permission, do the thing!
            Toast.makeText(this, "TODO: Location things", Toast.LENGTH_LONG).show()
            startLocationUpdates()

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
    private fun hasLocationPermissions():Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    protected fun startLocationUpdates() {

        // Create the location request to start receiving updates

        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.setInterval(INTERVAL)
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback,
            Looper.myLooper())

    }


    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // do work here
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        // New location has now been determined
        var latitude = location.latitude
        var longitude = location.longitude
        //latitude = mLastLocation.latitude
        //longitude = mLastLocation.longitude
        val phone = phone
        val queu = Volley.newRequestQueue(applicationContext)
        var url: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/TrackDP.php"
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
                    val params = java.util.HashMap<String, String>()
                    params.put("phone",phone.toString())
                    params.put("latitude", latitude.toString())
                    params.put("longitude", longitude.toString())
                    return params
                }
            }
        queu.add(postRequest)
    }

    protected fun createMarker(latitude : Double, longitude:Double,  title:String, snippets: String): Marker {
        var map = googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .title(title).snippet(snippets)
        )
        map.showInfoWindow()
        return map

    }


}
