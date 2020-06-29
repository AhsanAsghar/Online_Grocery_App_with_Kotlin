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
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import org.json.JSONArray
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

class MapForHiringDeliveryPerson : AppCompatActivity() {

    var phone:String? = null
    lateinit var mapFragment : SupportMapFragment
    private val RC_LOCATION_PERM = 124
    var latitude : String = ""
    var longitude : String = ""
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var googleMap : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_for_hiring_delivery_person)
        if (intent.getStringExtra("phone") != null) {
            phone = intent.getStringExtra("phone")
        } else {
            phone = "+923450694449"
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationTask()
        val handler:Handler=Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {

             /*   val queu = Volley.newRequestQueue(applicationContext)
                var url: String =
                    "https://grocerymedicineapp.000webhostapp.com/PHPfiles/NearestDeliveryPersonFinding.php"
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
                            params.put("source_latitude",latitude)
                            params.put("source_longitude",longitude)
                            return params
                        }
                    }
                queu.add(postRequest)*/


                val queue = Volley.newRequestQueue(applicationContext)
                val url_get: String =
                    "https://grocerymedicineapp.000webhostapp.com/PHPfiles/NearestDeliveryPersonFinding.php?source_latitude =$latitude&source_longitude=$longitude"
                var request: StringRequest = StringRequest(url_get, Response.Listener { response ->
                    Log.d("json", response.toString())
                    //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
                    //var json : JSONArray = response.getJSONArray(0)
                    val jObject = JSONObject(response.toString())
                    val jsonArray : JSONArray = jObject?.getJSONArray("response")!!
                    Log.d("json",jsonArray.toString())
                    val a = jsonArray.length()
                    Log.d("json",a.toString())
                }, Response.ErrorListener { error ->
                    Log.d("json", error.toString())
                    Toast.makeText(this@MapForHiringDeliveryPerson, error.toString(), Toast.LENGTH_SHORT)
                        .show()
                })
                queue.add((request))



                mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_map_for_hiring_delivery_person) as SupportMapFragment
                mapFragment.getMapAsync(OnMapReadyCallback {
                    googleMap = it
                })
                handler.postDelayed(this,3000)
            }
        }, 3000)

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
                    val geocoder:Geocoder
                    val addresses: List<Address>
                    geocoder = Geocoder(this, Locale.getDefault())
                    addresses = geocoder.getFromLocation(location.latitude,location.longitude,1)
                    val address:String= addresses.get(0).getAddressLine(0)
                    val city:String = addresses.get(0).locality
                    val state: String  = addresses.get(0).getAdminArea();
                    val country: String = addresses.get(0).getCountryName();

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
    private fun hasLocationPermissions():Boolean {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)
    }
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
                latitude=mLastLocation.latitude.toString()
                longitude = mLastLocation.longitude.toString()

            }

        }
    }

}
