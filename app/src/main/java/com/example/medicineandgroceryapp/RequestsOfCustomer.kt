package com.example.medicineandgroceryapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class RequestsOfCustomer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests_of_customer)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_requests_of_customers);
        setSupportActionBar(mToolbar)
        val recycleOfCategory = findViewById<RecyclerView>(R.id.recyclerView_requests_of_customers)
        recycleOfCategory.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        var idOfStore: String? = null
        val users = ArrayList<DataClassForRequestsOfCustomer>()
        val phone = intent.getStringExtra("phone")
        //get Store ID
        val queue = Volley.newRequestQueue(this)
        val url_id_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/getStoreID.php?phone=$phone"
        val request_id_get : StringRequest = StringRequest(url_id_get, Response.Listener {
                response ->
            val result  = response.toString().split(":").toTypedArray()
            val yesORno = result[1].substring(1,result[1].length - 2)
            idOfStore = yesORno
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@RequestsOfCustomer,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request_id_get))
        //End getting Store ID
        val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/getRequestsForStoreOwner.php?phone=$phone"
        val request : StringRequest = StringRequest(url_get, Response.Listener {
                response ->
            Log.d("json",response.toString())
            //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
            //var json : JSONArray = response.getJSONArray(0)
            val jObject : JSONObject = JSONObject(response.toString())
            val jsonArray : JSONArray = jObject?.getJSONArray("response")!!
            Log.d("json",jsonArray.toString())
            val a = jsonArray.length()
            Log.d("json",a.toString())
            for(y in 1..a-1){
                Log.d("list", "in")
                val customer_name = jsonArray.getJSONObject(y).getString("name")
                val customer_phone = jsonArray.getJSONObject(y).getString("customer_mobile_number")
                val pimageString = jsonArray.getJSONObject(y).getString("profile_pic")
                var customer_img : Bitmap? = null
                if(pimageString != "null") {
                    customer_img = stringToBitmap(pimageString)
                }
                if(idOfStore != null){
                    users.add(DataClassForRequestsOfCustomer(customer_img,customer_name,customer_phone,this,phone,
                        idOfStore!!
                    ))
                }
            }
            val adapter = CustomAdapterClassForRequestsOfCustomer(users)
            recycleOfCategory.adapter = adapter
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@RequestsOfCustomer,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request))

    }

    fun stringToBitmap(imageInString : String) : Bitmap {
        val imageBytes = Base64.decode(imageInString,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return image
    }
}
