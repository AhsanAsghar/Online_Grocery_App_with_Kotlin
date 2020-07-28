package com.example.medicineandgroceryapp

import android.Manifest
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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

class MapForHiringDeliveryPerson : AppCompatActivity(), GoogleMap.OnInfoWindowClickListener,
OnMapReadyCallback {

    var phone:String? = null
    lateinit var mapFragment : SupportMapFragment
    private val RC_LOCATION_PERM = 124
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var googleMap : GoogleMap
    var store_latitude:String = ""
    var store_longitude:String = ""
    var customer_latitude:String = ""
    var customer_longitude:String = ""
    val hireButton  = findViewById<Button>(R.id.hire_delivery_person)
   // var store_id2 : String = ""
    var list1 = mutableListOf<HiringModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_for_hiring_delivery_person)
        if (intent.getStringExtra("phone") != null ) {
            phone = intent.getStringExtra("phone")

        } else {
            phone = "+923450694449"
        }
        hireButton.isEnabled = false

        gettingStoreAddress()
        gettingCustomerAddress()

        mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_map_for_hiring_delivery_person) as SupportMapFragment


       // mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
       // locationTask()
        val handler:Handler=Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                    val queue = Volley.newRequestQueue(applicationContext)
                    val url_get: String =
                        "https://grocerymedicineapp.000webhostapp.com/PHPfiles/NearestDeliveryPersonFinding.php?source_latitude=$store_latitude&source_longitude=$store_longitude"
                    var request: StringRequest =
                        StringRequest(url_get, Response.Listener { response ->
                            Log.d("json", response.toString())
                            //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
                            //var json : JSONArray = response.getJSONArray(0)
                            val jObject: JSONObject = JSONObject(response.toString())
                            val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
                            Log.d("json", jsonArray.toString())
                            val a: Int = jsonArray.length()
                            for (y in 1..a - 1) {
                                Log.d("list", "in")
                                val dp_id = jsonArray.getJSONObject(y).getString("dp_id")
                                val dp_phone = jsonArray.getJSONObject(y).getString("phone_number")
                                val dp_latitude = jsonArray.getJSONObject(y).getString("latitude")
                                val dp_longitude = jsonArray.getJSONObject(y).getString("longitude")
                                //val dp_name = jsonArray.getJSONObject(y).getString("dp_name")
                                val distance = jsonArray.getJSONObject(y).getString("distance")
                                var location1 =
                                    LatLng(dp_latitude.toDouble(), dp_longitude.toDouble())
                                Log.d("data", dp_id + " " + " " + distance)


                                val distance1 = distance.toDouble()
                                val distance2 = "%.2f".format(distance1)
                                //distance.toDouble() =".%4f".format(distance.toDouble())
                                if (dp_id != null && dp_latitude!=null && dp_longitude !=null  && distance !=null) {
                                    createMarker(
                                        dp_latitude.toDouble(),
                                        dp_longitude.toDouble(),
                                        "Delivery Person",
                                        "Distance:" + distance2 + "KM"
                                    )
                                    val hiringModel = HiringModel()
                                    hiringModel.latitude=dp_latitude
                                    hiringModel.longitude=dp_longitude
                                    hiringModel.dp_id=dp_id.toInt()
                                    hiringModel.phone=dp_phone
                                    list1.add(hiringModel)

                                    Toast.makeText(
                                        this@MapForHiringDeliveryPerson,
                                        "Distance:" + distance2 + "KM",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else{
                                    Toast.makeText(this@MapForHiringDeliveryPerson,"Delivery Person is not Available in your Area at this time.!", Toast.LENGTH_LONG).show()
                                }
                            }
                            Log.d("list", "out")
                        }, Response.ErrorListener { error ->
                            Log.d("json", error.toString())
                            Toast.makeText(
                                this@MapForHiringDeliveryPerson,
                                error.toString(),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        })
                    queue.add((request))

                handler.postDelayed(this,10000)
            }
        }, 10000)

    }
    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        map?.setOnInfoWindowClickListener(this)
    }

    override fun  onInfoWindowClick(marker: Marker) {
        //let find marker from the list that have same lat long as one that has been clicked then we'll send found marker which also clicked marker dp id to GCM for notifications
        var  found=HiringModel()
        for(item in list1) {
            if (item.latitude == marker.position.latitude.toString() && item.longitude == marker.position.longitude.toString()) {
                found = item
            }
        }
        Toast.makeText(this, "dp id clicked = "+found.dp_id, Toast.LENGTH_SHORT).show()
       /* hireButton.isEnabled = true
        hireButton.setOnClickListener {
            v ->
            val queue = Volley.newRequestQueue(this)
            val url_change_status: String =
                "https://grocerymedicineapp.000webhostapp.com/PHPfiles/changeStatusStoreSend.php?phone=$phone&dp_phone=${found.phone}&status=dsend"
            val request_change_status: StringRequest =
                StringRequest(url_change_status, Response.Listener { response ->
                    val result = response.toString().split(":").toTypedArray()
                    val yesORno = result[1].substring(1, result[1].length - 2)
                    if (yesORno.equals("YES")) {

                    }
                }, Response.ErrorListener { error ->
                    Log.d("json", error.toString())
                    Toast.makeText(this@MapForHiringDeliveryPerson, error.toString(), Toast.LENGTH_SHORT).show()
                })
            queue.add((request_change_status))

            val intent = Intent(this@MapForHiringDeliveryPerson,UserNavigation::class.java)
            intent.putExtra("phone",phone)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this.finish()
        }*/
    }



  /* protected fun gettingStoreId() {
        val queue = Volley.newRequestQueue(applicationContext)
        val url_get: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/gettingStoreId.php?phone=$phone"
        var request: StringRequest = StringRequest(url_get, Response.Listener { response ->
            Log.d("json", response.toString())
            //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
            //var json : JSONArray = response.getJSONArray(0)
            val jObject: JSONObject = JSONObject(response.toString())
            val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
            val jsonObject: JSONObject = jsonArray.getJSONObject(0);
            store_id2 = jsonObject.getString("store_id")
            //Toast.makeText(this@MapForHiringDeliveryPerson, "store id:"+store_id2, Toast.LENGTH_LONG).show()


        }, Response.ErrorListener { error ->
            Log.d("json", error.toString())
            Toast.makeText(this@MapForHiringDeliveryPerson, error.toString(), Toast.LENGTH_SHORT)
                .show()
        })
        queue.add((request))
    }*/




     fun gettingStoreAddress() {
        val store_location:EditText =findViewById(R.id.store_location)
        val queue = Volley.newRequestQueue(applicationContext)
        val url_get: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/StoreAddressGetting.php?phone=$phone"
        var request: StringRequest = StringRequest(url_get, Response.Listener { response ->
            Log.d("json", response.toString())
            //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
            //var json : JSONArray = response.getJSONArray(0)
            val jObject: JSONObject = JSONObject(response.toString())
            val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
            val jsonObject: JSONObject = jsonArray.getJSONObject(0);
            store_latitude = jsonObject.getString("latitude")
            store_longitude = jsonObject.getString("longitude")
            //latitude = "32.1474"
            //longitude = "74.21"

            val location2 = LatLng(store_latitude.toDouble(),store_longitude.toDouble())
            //Map Marker Working
            mapFragment.getMapAsync(OnMapReadyCallback {
                googleMap = it
                createMarker(store_latitude.toDouble(), store_longitude.toDouble(), "My Store Location", "")
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location2))
                //googleMap.animateCamera(CameraUpdateFactory.zoomTo(16f))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location2,13f))
                Toast.makeText(this@MapForHiringDeliveryPerson, "Marker Updated", Toast.LENGTH_SHORT).show()

            })


            val geocoder:Geocoder
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
            Toast.makeText(this@MapForHiringDeliveryPerson, error.toString(), Toast.LENGTH_SHORT)
                .show()
        })
        queue.add((request))
        //To change body of created functions use File | Settings | File Templates.
    }
    fun gettingCustomerAddress(){
        val customer_location:EditText =findViewById(R.id.customer_location)
        val queue = Volley.newRequestQueue(applicationContext)
        val url_get: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/GettingCustomerAddress.php?phone=$phone"
        var request: StringRequest = StringRequest(url_get, Response.Listener { response ->
            Log.d("json", response.toString())
            //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
            //var json : JSONArray = response.getJSONArray(0)
            val jObject: JSONObject = JSONObject(response.toString())
            val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
            val jsonObject: JSONObject = jsonArray.getJSONObject(0);
            customer_latitude = jsonObject.getString("latitude")
            customer_longitude = jsonObject.getString("longitude")
            //latitude = "32.1474"
            //longitude = "74.21"

            val geocoder:Geocoder
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
            Toast.makeText(this@MapForHiringDeliveryPerson, error.toString(), Toast.LENGTH_SHORT)
                .show()
        })
        queue.add((request))
        //To change body of created functions use File | Settings | File Templates.
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

    



    /* override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
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
     }*/

}
