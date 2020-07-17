package com.example.medicineandgroceryapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class DeliveryPersonDetailAndDetection : AppCompatActivity() {
    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap: GoogleMap
    var phone : String? = null
    var latitude:String = ""
    var longitude:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_person_detail_and_detection)
        if (intent.getStringExtra("phone") != null && intent.getStringExtra("name") != null) {

        } else {
            phone = "+923167617639"
        }
        mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_delivery_person_detail_and_detection) as SupportMapFragment

        gettingStoreAddress()
        gettingCustomerAddress()



        val handler: Handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                val queue = Volley.newRequestQueue(applicationContext)
                val url_get: String =
                    "https://grocerymedicineapp.000webhostapp.com/PHPfiles/TrackDPGet.php?phone=$phone"
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
                                    .title("Marker Title")
                                marker = googleMap.addMarker(options)
                            } else {
                                location = LatLng(10.0,24.7)
                                marker.setPosition(location)
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
                    Toast.makeText(this@DeliveryPersonDetailAndDetection, error.toString(), Toast.LENGTH_SHORT)
                        .show()
                })
                queue.add((request))
                handler.postDelayed(this,3000)
            }
        }, 3000)





        val infoButton = findViewById(R.id.info) as Button
        infoButton.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.map_dialogue,null)
            val cnic = dialogView.findViewById<TextView>(R.id.cnic)
            val phoneNumber = dialogView.findViewById<TextView>(R.id.phone_no)
            dialog.setView(dialogView)
            dialogView.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.show()
        }
       /* mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_delivery_person_detail_and_detection) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
        })*/
    }


    fun gettingStoreAddress() {
        val store_location: EditText = findViewById(R.id.coming_towards)
        val queue = Volley.newRequestQueue(applicationContext)
        val url_get: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/GettingStoreAddressForTrackingActivity.php?phone=$phone"
        var request: StringRequest = StringRequest(url_get, Response.Listener { response ->
            Log.d("json", response.toString())
            //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
            //var json : JSONArray = response.getJSONArray(0)
            val jObject: JSONObject = JSONObject(response.toString())
            val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
            val jsonObject: JSONObject = jsonArray.getJSONObject(0);
            latitude = jsonObject.getString("latitude")
            longitude = jsonObject.getString("longitude")
            //latitude = "32.1474"
            //longitude = "74.21"

            val location2 = LatLng(latitude.toDouble(),longitude.toDouble())
            //Map Marker Working
            mapFragment.getMapAsync(OnMapReadyCallback {
                googleMap = it
                createMarker(latitude.toDouble(), longitude.toDouble(), "My Store Location", "")
                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(location2))
                //googleMap.animateCamera(CameraUpdateFactory.zoomTo(16f))
                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location2,13f))
                Toast.makeText(this@DeliveryPersonDetailAndDetection, "Marker Updated", Toast.LENGTH_SHORT).show()

            })


            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(this, Locale.getDefault())
            addresses = geocoder.getFromLocation(latitude.toDouble(),longitude.toDouble(),1)
            val address:String= addresses.get(0).getAddressLine(0)
            val city:String = addresses.get(0).locality
            val state: String  = addresses.get(0).getAdminArea();
            val country: String = addresses.get(0).getCountryName();

            store_location.setText(address+","+city)


        }, Response.ErrorListener { error ->
            Log.d("json", error.toString())
            Toast.makeText(this@DeliveryPersonDetailAndDetection, error.toString(), Toast.LENGTH_SHORT)
                .show()
        })
        queue.add((request))
        //To change body of created functions use File | Settings | File Templates.
    }
    fun gettingCustomerAddress(){
        val customer_location: EditText = findViewById(R.id.customer_destination)
        val queue = Volley.newRequestQueue(applicationContext)
        val url_get: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/GettingCustomerAddressForTrackingActivity.php?phone=$phone"
        var request: StringRequest = StringRequest(url_get, Response.Listener { response ->
            Log.d("json", response.toString())
            //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
            //var json : JSONArray = response.getJSONArray(0)
            val jObject: JSONObject = JSONObject(response.toString())
            val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
            val jsonObject: JSONObject = jsonArray.getJSONObject(0);
            latitude = jsonObject.getString("latitude")
            longitude = jsonObject.getString("longitude")
            //latitude = "32.1474"
            //longitude = "74.21"


            val location2 = LatLng(latitude.toDouble(),longitude.toDouble())
            //Map Marker Working
            mapFragment.getMapAsync(OnMapReadyCallback {
                googleMap = it
                createMarker(latitude.toDouble(), longitude.toDouble(), "Customer Location", "")
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location2))
                //googleMap.animateCamera(CameraUpdateFactory.zoomTo(16f))
                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location2,13f))
                //Toast.makeText(this@DeliveryPersonDetailAndDetection, "Marker Updated", Toast.LENGTH_SHORT).show()

            })


            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(this, Locale.getDefault())
            addresses = geocoder.getFromLocation(latitude.toDouble(),longitude.toDouble(),1)
            val address:String= addresses.get(0).getAddressLine(0)
            val city:String = addresses.get(0).locality
            val state: String  = addresses.get(0).getAdminArea();
            val country: String = addresses.get(0).getCountryName();

            customer_location.setText(address+","+city)


        }, Response.ErrorListener { error ->
            Log.d("json", error.toString())
            Toast.makeText(this@DeliveryPersonDetailAndDetection, error.toString(), Toast.LENGTH_SHORT)
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


}
