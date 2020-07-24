package com.example.medicineandgroceryapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class RequestsForDP : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_for_dp)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_notification);
        setSupportActionBar(mToolbar)
        val recycleOfCategory = findViewById<RecyclerView>(R.id.recyclerView_notification)
        recycleOfCategory.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val phone = intent.getStringExtra("phone")
        val users = ArrayList<DataClassForRequestToDp>()

        //Get requests to Delicery Person
        val progress = ProgressBar(this)
        progress.startLoading(true,"Please Wait - Storedeck is getting requests")
        val queue = Volley.newRequestQueue(this)
        val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/getRequestsToDeliveryPerson.php?phone=$phone"
        val request : StringRequest = StringRequest(url_get, Response.Listener {
                response ->
            val result  = response.toString().split(":").toTypedArray()
            val noResponse = result[1].substring(1,result[1].length - 2)
            Log.d("piq1",noResponse)
            if(noResponse.equals("NO")){
                Log.d("problem",noResponse)
            }else {
                Log.d("json", response.toString())
                //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
                //var json : JSONArray = response.getJSONArray(0)
                val jObject: JSONObject = JSONObject(response.toString())
                val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
                Log.d("json", jsonArray.toString())
                val a = jsonArray.length()
                Log.d("json", a.toString())
                for (y in 1..a - 1) {
                    Log.d("list", "in")
                    val store_name = jsonArray.getJSONObject(y).getString("store_name")
                    val pimageString = jsonArray.getJSONObject(y).getString("store_image")
                    var store_image: Bitmap? = null
                    if (pimageString != "null") {
                        store_image = stringToBitmap(pimageString)
                        users.add(DataClassForRequestToDp(store_image,store_name,this,phone))
                    }
                    val adapter = CustomAdapterClassForRequestToDp(users)
                    recycleOfCategory.adapter = adapter
                }
                progress.dismissDialog()
            }
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@RequestsForDP,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request))


        //End getting reequest to delivery person
    }

    fun stringToBitmap(imageInString : String) : Bitmap {
        val imageBytes = Base64.decode(imageInString,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return image
    }
}

