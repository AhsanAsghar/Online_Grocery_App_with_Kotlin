package com.example.medicineandgroceryapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
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

class cart_items : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_items)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_cart_items);
        setSupportActionBar(mToolbar)
        val recycleOfCategory = findViewById<RecyclerView>(R.id.recyclerView_cart_items)
        recycleOfCategory.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val users = ArrayList<DataClassForCartItems>()

        //get status
        val customerPhone = intent.getStringExtra("phone")
        val store_id = intent.getStringExtra("id")
        val store_image = intent.getParcelableExtra<Bitmap>("image")
        val store_name = intent.getStringExtra("name")
        val trackButton : Button = findViewById(R.id.track_delivery_person)
        val queue = Volley.newRequestQueue(this)
        val requestButton : Button = findViewById(R.id.send_request)
        val url_get_status : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/getStatusOfRequest.php?storeid=$store_id&phone=$customerPhone"
        val request_status : StringRequest = StringRequest(url_get_status, Response.Listener {
                response ->
            Log.d("json",response.toString())
            val result  = response.toString().split(":").toTypedArray()
            val yesORno = result[1].substring(1,result[1].length - 2)
            Log.d("piq1",yesORno)
            findViewById<ImageView>(R.id.storeImageCartItem).setImageBitmap(store_image)
            findViewById<TextView>(R.id.store_name_cart_items).setText(store_name)
            if(yesORno.equals("NO")){
                Log.d("problem",yesORno)
            }else if(yesORno.equals("NULL")){
                findViewById<TextView>(R.id.request_status_cart_items).setText("Please Send request")
            }else if(yesORno.equals("send")){
                requestButton.visibility = View.GONE
                findViewById<TextView>(R.id.request_status_cart_items).setText("Pending")
            }
            else if(yesORno.equals("d_send") || yesORno.equals("d_accept")){
                requestButton.visibility = View.GONE
                findViewById<TextView>(R.id.request_status_cart_items).setText("Delivery person is being hired")
            }else if(yesORno.equals("accept")){
                requestButton.visibility = View.GONE
                trackButton.visibility = View.VISIBLE
                findViewById<TextView>(R.id.request_status_cart_items).setText("On the way")
            }
            else if(yesORno.equals("reject")) {
                findViewById<TextView>(R.id.request_status_cart_items).setText("Order Canceled")
            } else{
                Toast.makeText(this@cart_items, yesORno,Toast.LENGTH_SHORT).show()
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
        val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/cartItemsGet.php?storeid=$store_id&phone=$customerPhone"
        val request : StringRequest = StringRequest(url_get, Response.Listener {
                response ->
            val result  = response.toString().split(":").toTypedArray()
            val yesORno = result[1].substring(1,result[1].length - 2)
            Log.d("piq",yesORno)
            if(yesORno.equals("NO")){
                Toast.makeText(this,yesORno,Toast.LENGTH_SHORT).show()
            }else{
                Log.d("json",response.toString())
                val jObject : JSONObject = JSONObject(response.toString())
                val jsonArray : JSONArray = jObject?.getJSONArray("response")!!
                Log.d("json",jsonArray.toString())
                val a = jsonArray.length()
                Log.d("json",a.toString())
                for(y in 0..a-1){
                    Log.d("list", "in")
                    val cart_id = jsonArray.getJSONObject(y).getString("cart_id")
                    val product_id = jsonArray.getJSONObject(y).getString("product_id")
                    val product_name = jsonArray.getJSONObject(y).getString("product_name")
                    val pimageString = jsonArray.getJSONObject(y).getString("product_image")
                    val product_img = stringToBitmap(pimageString)
                    val productPrice = jsonArray.getJSONObject(y).getString("product_price")
                    users.add(DataClassForCartItems(product_img,product_name, productPrice,cart_id,this))
                }
                val adapter = CustomAdapterClassForCartItems(users)
                recycleOfCategory.adapter = adapter
            }

        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@cart_items,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request))
    }

    fun stringToBitmap(imageInString : String) : Bitmap {
        val imageBytes = Base64.decode(imageInString,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return image
    }
}
