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

class cart_items : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_items)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_cart_items);
        setSupportActionBar(mToolbar)
        val recycleOfCategory = findViewById<RecyclerView>(R.id.recyclerView_cart_items)
        recycleOfCategory.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val resid = R.id.cart_item_photo
        val users = ArrayList<DataClassForCartItems>()

        //get status
        val phone = "+923450694449"
        val store_id = 8
        var status = "Request not yet send"
        val queue = Volley.newRequestQueue(this)
        val url_get_status : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/getStatusOfRequest.php?storeid=$store_id&phone=$phone"
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
                //users.add(DataClassForCartItems(resid,"Al Habib", status))
            }
            val adapter = CustomAdapterClassForCartItems(users)
            recycleOfCategory.adapter = adapter
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@cart_items,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request_status))
        // end getting status
        val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/cartItemsGet.php?storeid=$store_id&phone=$phone"
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
                users.add(DataClassForCartItems(product_img,product_name, productPrice))
            }
            val adapter = CustomAdapterClassForCartItems(users)
            recycleOfCategory.adapter = adapter
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@cart_items,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request))
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
