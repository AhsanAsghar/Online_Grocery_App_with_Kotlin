package com.example.medicineandgroceryapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class RequestDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_detail)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_request_details);
        setSupportActionBar(mToolbar)
        val recycleOfCategory = findViewById<RecyclerView>(R.id.recyclerView_request_detail)
        recycleOfCategory.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val resid = R.id.cart_item_photo_request_detail
        val status_field: TextView = findViewById(R.id.status_request_detail)
        val priceOfDelivery: TextView = findViewById(R.id.price_of_delivery_charges_request_detail)
        val users = ArrayList<DataClassForRequestDetails>()

        //get status
        val customer_phone = "+923450694449"
        val store_id = 8
        var status = "Request not yet send"
        val queue = Volley.newRequestQueue(this)
        val url_get_status : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/getStatusOfRequest.php?storeid=$store_id&phone=$customer_phone"
        val request_status : StringRequest = StringRequest(url_get_status, Response.Listener {
                response ->
            Log.d("json",response.toString())
            val jObject : JSONObject = JSONObject(response.toString())
            val jsonArray : JSONArray = jObject?.getJSONArray("response")!!
            Log.d("json",jsonArray.toString())
            val a = jsonArray.length()
            Log.d("json",a.toString())
            for(y in 0..a-1){
                status  = jsonArray.getJSONObject(y).getString("status")
                Log.d("status", status)
                if(status.equals("send")){
                    status = "Requested"
                }
                status_field.setText(status)
            }
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@RequestDetail,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request_status))
        //End getting Status

        //Get products
        val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/cartItemsGet.php?storeid=$store_id&phone=$customer_phone"
        val request : StringRequest = StringRequest(url_get, Response.Listener {
                response ->
            Log.d("json",response.toString())
            val jObject : JSONObject = JSONObject(response.toString())
            val jsonArray : JSONArray = jObject?.getJSONArray("response")!!
            Log.d("json",jsonArray.toString())
            val a = jsonArray.length()
            Log.d("json",a.toString())
            for(y in 0..a-1){
                Log.d("list", "in")
                val product_id = jsonArray.getJSONObject(y).getString("product_id")
                val product_name = jsonArray.getJSONObject(y).getString("product_name")
                val pimageString = jsonArray.getJSONObject(y).getString("product_image")
                val product_img = stringToBitmap(pimageString)
                val productPrice = jsonArray.getJSONObject(y).getString("product_price")
                users.add(DataClassForRequestDetails(product_img,product_name, productPrice))
            }
            val adapter = CustomAdapterClassForRequestDetail(users)
            recycleOfCategory.adapter = adapter
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@RequestDetail,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request))
        //end getting products
        //get hiring status of store owner
        val url_get_hiring_status : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkHiringStatusOfStoreOwner.php?storeid=$store_id&phone=$customer_phone"
        val request_hiring_status : StringRequest = StringRequest(url_get_hiring_status, Response.Listener {
                response ->
            var statusOfHiring  = response.toString().split(":").toTypedArray()
            val hiring = statusOfHiring[1].substring(1,statusOfHiring[1].length - 2)
            if(hiring.equals("Null")){
                priceOfDelivery.setText("Hire Delivery Person")
            }else{

            }
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@RequestDetail,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request_hiring_status))
        //end getting hiring status of store owner
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nearest_stores,menu)
        return super.onCreateOptionsMenu(menu)
    }
    fun stringToBitmap(imageInString : String) : Bitmap {
        val imageBytes = Base64.decode(imageInString,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return image
    }
}
