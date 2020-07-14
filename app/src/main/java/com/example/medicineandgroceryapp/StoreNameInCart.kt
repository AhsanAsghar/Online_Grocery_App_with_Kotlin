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

class StoreNameInCart : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_name_in_cart)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_store_name_in_cart);
        setSupportActionBar(mToolbar)
        val recycleOfCategory = findViewById<RecyclerView>(R.id.recyclerView_store_name_in_cart)
        recycleOfCategory.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val users = ArrayList<DataClassStoreNameInCart>()
        val phone = intent.getStringExtra("phone")
        val queue = Volley.newRequestQueue(this)
        val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/cartStoreGet.php?phone=$phone"
        val request : StringRequest = StringRequest(url_get, Response.Listener {
                response ->
            Log.d("json",response.toString())
            if(response.toString() == "{\"response\":[null]}"){
                Log.d("json",response.toString())
            }else{
                //var json : JSONArray = response.getJSONArray(0)
                val jObject : JSONObject = JSONObject(response.toString())
                val jsonArray : JSONArray = jObject?.getJSONArray("response")!!
                Log.d("json",jsonArray.toString())
                val a = jsonArray.length()
                Log.d("json",a.toString())
                for(y in 1..a-1){
                    Log.d("list", "in")
                    val store_name = jsonArray.getJSONObject(y).getString("store_name")
                    val store_id = jsonArray.getJSONObject(y).getString("store_id")
                    val pimageString = jsonArray.getJSONObject(y).getString("store_image")
                    val store_img = stringToBitmap(pimageString)
                    users.add(DataClassStoreNameInCart(store_img,store_name,store_id,this))
                }
                val adapter = CustomAdapterClassForStoreNameInCart(users)
                recycleOfCategory.adapter = adapter
            }

        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@StoreNameInCart,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request))
    }

    fun stringToBitmap(imageInString : String) : Bitmap {
        val imageBytes = Base64.decode(imageInString,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return image
    }
}
