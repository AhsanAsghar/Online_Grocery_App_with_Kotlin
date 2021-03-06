package com.example.medicineandgroceryapp

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.Request
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_delivery_person_accept_reject.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class DeliveryPersonAcceptReject : AppCompatActivity() {
    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap: GoogleMap
    var phone:String? = null
    var store_latitude:String = ""
    var store_longitude:String = ""
    var customer_latitude:String = ""
    var customer_longitude:String = ""
    var store_phone : String? = null
    var customer_phone : String? = null

    var store_id : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_person_accept_reject)
        val acceptButton = findViewById<Button>(R.id.accept)
        val rejectButton = findViewById<Button>(R.id.reject)
        val trackButton = findViewById<Button>(R.id.TrackOrder)
        mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_delivery_person_accept_reject) as SupportMapFragment

        //phone = "+923004579023"
        //store_id = "34"
                phone = intent.getStringExtra("phone")
                store_id = intent.getStringExtra("id")
       // Toast.makeText(this, "store_id:" +store_id, Toast.LENGTH_LONG).show()



        val queue = Volley.newRequestQueue(applicationContext)
        val url_get: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkstatusUpdated.php?phone=$phone&store_id=$store_id"
        //val url_get: String =
          //  "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkstatus.php?phone=$phone"
        var request: StringRequest = StringRequest(url_get, Response.Listener { response ->
            Log.d("json", response.toString())
            //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
            //var json : JSONArray = response.getJSONArray(0)
            val jObject: JSONObject = JSONObject(response.toString())
            val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
            val jsonObject: JSONObject = jsonArray.getJSONObject(0);
            val status = jsonObject.getString("status")
            if(status.equals("accept")){
                trackButton.isEnabled = true
                acceptButton.isEnabled = false
                rejectButton.isEnabled = false
            }
            else{
                trackButton.isEnabled = false
                acceptButton.isEnabled = true
                rejectButton.isEnabled = true
            }


        }, Response.ErrorListener { error ->
            Log.d("json", error.toString())
            Toast.makeText(this@DeliveryPersonAcceptReject, error.toString(), Toast.LENGTH_SHORT)
                .show()
        })
        queue.add((request))

        gettingCustomerPhone()
        gettingStoreAddress()
        gettingCustomerAddress()
        gettingStorePhone()

        acceptButton.setOnClickListener { v ->
            availabilityoff()
            val queue = Volley.newRequestQueue(this)
            val url_change_status: String =
                "https://grocerymedicineapp.000webhostapp.com/PHPfiles/changeStatusDpAccept.php?phone=$phone&store_phone=$store_phone&status=daccept"
            val request_change_status: StringRequest =
                StringRequest(url_change_status, Response.Listener { response ->
                    val result = response.toString().split(":").toTypedArray()
                    val yesORno = result[1].substring(1, result[1].length - 2)
                    if (yesORno.equals("YES")) {
                        sendAcceptRejectNotificationToOwnerFromDp(store_phone.toString(),1)
                    }
                }, Response.ErrorListener { error ->
                    Log.d("json", error.toString())
                    Toast.makeText(this@DeliveryPersonAcceptReject, error.toString(), Toast.LENGTH_SHORT).show()
                })
            queue.add((request_change_status))

            val intent = Intent(this@DeliveryPersonAcceptReject,FinalActivityForDeliveryPerson::class.java)
            intent.putExtra("phone",phone)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this.finish()

        }
        rejectButton.setOnClickListener { v->
            val queue = Volley.newRequestQueue(this)
            val url_change_status: String =
                "https://grocerymedicineapp.000webhostapp.com/PHPfiles/changeStatusDpReject.php?phone=$phone&store_phone=$store_phone&status=send"
            val request_change_status: StringRequest =
                StringRequest(url_change_status, Response.Listener { response ->
                    val result = response.toString().split(":").toTypedArray()
                    val yesORno = result[1].substring(1, result[1].length - 2)
                    if (yesORno.equals("YES")) {
                        sendAcceptRejectNotificationToOwnerFromDp(store_phone.toString(),0)
                    }
                }, Response.ErrorListener { error ->
                    Log.d("json", error.toString())
                    Toast.makeText(this@DeliveryPersonAcceptReject, error.toString(), Toast.LENGTH_SHORT).show()
                })
            queue.add((request_change_status))
            this.finish()
            deleteDpIdToOrders()
        }
        trackButton.setOnClickListener { v->
            val intent = Intent(this@DeliveryPersonAcceptReject,FinalActivityForDeliveryPerson::class.java)
            intent.putExtra("phone",phone)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this.finish()
        }
        }

    fun availabilityoff(){

        val queue = Volley.newRequestQueue(this)
        val url_change_status: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/writeAvailabilityoff.php?phone=$phone"
        val request_change_status: StringRequest =
            StringRequest(url_change_status, Response.Listener { response ->
                val result = response.toString().split(":").toTypedArray()
                val yesORno = result[1].substring(1, result[1].length - 2)
                if (yesORno.equals("YES")) {
                    Toast.makeText(this@DeliveryPersonAcceptReject,"Availability Off", Toast.LENGTH_LONG).show()
                }
            }, Response.ErrorListener { error ->
                Log.d("json", error.toString())
                Toast.makeText(this@DeliveryPersonAcceptReject, error.toString(), Toast.LENGTH_SHORT).show()
            })
        queue.add((request_change_status))
    }

    fun gettingStoreAddress(){
        val store_location =  findViewById<EditText>(R.id.coming_towards)
        val queue = Volley.newRequestQueue(applicationContext)
        val url_get: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/gettingStoreAddressForDpUpdated.php?store_id=$store_id"
        //val url_get: String =
          //  "https://grocerymedicineapp.000webhostapp.com/PHPfiles/gettingStoreAddressForDp.php?phone=$phone"
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
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location2))
                //googleMap.animateCamera(CameraUpdateFactory.zoomTo(16f))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location2,13f))
                Toast.makeText(this@DeliveryPersonAcceptReject, "Marker Updated", Toast.LENGTH_SHORT).show()

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
            Toast.makeText(this@DeliveryPersonAcceptReject, error.toString(), Toast.LENGTH_SHORT)
                .show()
        })
        queue.add((request))
    }
    fun gettingCustomerAddress(){
        val customer_location = findViewById<EditText>(R.id.customer_destination)
        val queue = Volley.newRequestQueue(applicationContext)
        //val url_get: String =
          //  "https://grocerymedicineapp.000webhostapp.com/PHPfiles/gettingustomerAddressForDp.php?phone=$phone"
        val url_get: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/gettingustomerAddressForDpUpdated.php?phone=$phone&store_id=$store_id"
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
                Toast.makeText(this@DeliveryPersonAcceptReject, "Marker Updated", Toast.LENGTH_SHORT).show()

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
            Toast.makeText(this@DeliveryPersonAcceptReject, error.toString(), Toast.LENGTH_SHORT)
                .show()
        })
        queue.add((request))
    }
    fun gettingStorePhone(){
        val queue = Volley.newRequestQueue(applicationContext)
        //val url_get: String =
          //  "https://grocerymedicineapp.000webhostapp.com/PHPfiles/gettingStorePhone.php?phone=$phone & store_id = $store_id"
        val url_get: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/gettingStorePhoneUpdated.php?store_id=$store_id"
        var request: StringRequest = StringRequest(url_get, Response.Listener { response ->
            Log.d("json", response.toString())
            //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
            //var json : JSONArray = response.getJSONArray(0)
            val jObject: JSONObject = JSONObject(response.toString())
            val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
            val jsonObject: JSONObject = jsonArray.getJSONObject(0);
            store_phone = jsonObject.getString("phone")
            Toast.makeText(this@DeliveryPersonAcceptReject, "Store Phone:"+store_phone, Toast.LENGTH_LONG ).show()

        }, Response.ErrorListener { error ->
            Log.d("json", error.toString())
            Toast.makeText(this@DeliveryPersonAcceptReject, error.toString(), Toast.LENGTH_SHORT)
                .show()
        })
        queue.add((request))
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
    fun sendAcceptRejectNotificationToOwnerFromDp(owner_id:String,flag:Int){
        val db = FirebaseFirestore.getInstance()
        val data = hashMapOf(
            "oid" to owner_id,
            "flag" to flag,
            "cid" to customer_phone,
            "stid" to store_id
        )
        db.collection("ReplyToOwnerByDp").document(owner_id)
            .set(data)
            .addOnSuccessListener { documentReference ->
            }
            .addOnFailureListener { e -> }
    }
   fun gettingCustomerPhone(){

       val queue = Volley.newRequestQueue(applicationContext)
       val url_get: String =
           "https://grocerymedicineapp.000webhostapp.com/PHPfiles/gettingCustomerPhone.php?phone=$phone&store_id=$store_id"
       var request: StringRequest = StringRequest(url_get, Response.Listener { response ->
           Log.d("json", response.toString())
           //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
           //var json : JSONArray = response.getJSONArray(0)
           val jObject: JSONObject = JSONObject(response.toString())
           val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
           val jsonObject: JSONObject = jsonArray.getJSONObject(0);
           customer_phone = jsonObject.getString("customer_phone")

           Toast.makeText(this@DeliveryPersonAcceptReject, "Customer_...Phone:"+customer_phone, Toast.LENGTH_LONG ).show()

       }, Response.ErrorListener { error ->
           Log.d("json", error.toString())
           Toast.makeText(this@DeliveryPersonAcceptReject, error.toString(), Toast.LENGTH_SHORT)
               .show()
       })
       queue.add((request))

   }
    fun deleteDpIdToOrders(){
        val queu = Volley.newRequestQueue(applicationContext)
        var url: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/DeleteDpId.php"
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
                    params.put("store_id", store_id.toString())
                    return params
                }
            }
        queu.add(postRequest)
    }
}
