package com.example.medicineandgroceryapp

import android.app.SearchManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList

class ItemsInCategory : AppCompatActivity() {

    var phone: String = "null"
    var name :String = "null"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_in_category)

        if (intent.getStringExtra("phone") != null && intent.getStringExtra("name") != null) {

        } else {
            phone = "+923004579023"
            name = "Ahsan"
        }

        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_items_in_category)
        setSupportActionBar(mToolbar)

        checkIfStoreOwner();
        chekIfDeliveryPerson();

        val recycleViewOfItemsInCategory = findViewById(R.id.recycler_items_in_category) as RecyclerView
        recycleViewOfItemsInCategory.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        val idFromIntent: Int = 6
        val users = ArrayList<DataItemsInCategoryParent>()
        val queue = Volley.newRequestQueue(this)
        val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/ItemsInCategory.php?id=$idFromIntent"
        val request : StringRequest = StringRequest(url_get, Response.Listener {
                response ->
            val jObject : JSONObject = JSONObject(response.toString())
            val jsonArray : JSONArray = jObject?.getJSONArray("response")!!
            Log.d("json",jsonArray.toString())
            val a = jsonArray.length()
            Log.d("json",a.toString())
            val listOfCategoryAdded = mutableListOf<String>()
            for(x in 0..a-1){
                val pxcategory = jsonArray.getJSONObject(x).getString("product_category")
                Log.d("list", "x" + x.toString())
                if(!listOfCategoryAdded.contains(pxcategory)) {
                    users.add(DataClassForDataItemsInCategoryLabel(pxcategory))
                    for(y in 0..a-1){
                        Log.d("list", "y" + y.toString())
                        val pycategory = jsonArray.getJSONObject(y).getString("product_category")
                        if(!listOfCategoryAdded.contains(pycategory) && pycategory.equals(pxcategory)){
                            Log.d("list", "in")
                            val pname = jsonArray.getJSONObject(y).getString("product_name")
                            val pprice = jsonArray.getJSONObject(y).getString("product_price")
                            val pimageString = jsonArray.getJSONObject(y).getString("product_image")
                            val pimage = stringToBitmap(pimageString)
                            val pid = jsonArray.getJSONObject(y).getString("product_id")
                            Log.d("id",pid)
                            users.add(DataClassForDataItemsInCategory(pimage,pname, pprice,this,pid.toInt(),2,"+923450694449"))
                        }
                    }
                    listOfCategoryAdded.add(pxcategory)
                    Log.d("list", listOfCategoryAdded.toString())
                }
            }
            val adapter = CustomAdapterForItemsInCategory(users)
            recycleViewOfItemsInCategory.adapter = adapter
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@ItemsInCategory,error.toString(),Toast.LENGTH_SHORT).show()
        })
        queue.add((request))

    }


    private fun chekIfDeliveryPerson() {
        val queu = Volley.newRequestQueue(applicationContext)
        var url: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkIfDeliveryPerson.php"
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
                    params.put("phone", phone)
                    return params
                }
            }
        queu.add(postRequest)


        var queue: RequestQueue
        queue = Volley.newRequestQueue(this)
        val URL: String =  "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkIfDeliveryPerson.php"
        val request = StringRequest(
            Request.Method.GET,
            URL,
            Response.Listener { response ->
                val deliveryPerson = response;
            },
            Response.ErrorListener { error -> Log.d("error", error.toString()) })
        queue.add(request)

    }

    private fun checkIfStoreOwner() {
        val queu = Volley.newRequestQueue(applicationContext)
        var url: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkIfStoreOwner.php"
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
                    params.put("phone", phone)
                    return params
                }
            }
        queu.add(postRequest)



        var queue: RequestQueue
        queue = Volley.newRequestQueue(this)
        val URL: String =  "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkIfStoreOwner.php"
        val request = StringRequest(
            Request.Method.GET,
            URL,
            Response.Listener { response ->
                val storeOwner = response;
            },
            Response.ErrorListener { error -> Log.d("error", error.toString()) })
        queue.add(request)
    }






    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_items_in_category,menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.search_items_in_category)
        val searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("",false)
                searchView.isIconified = true
                Toast.makeText(applicationContext,"looking for $query",Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(applicationContext,"looking fotr $newText", Toast.LENGTH_SHORT).show()
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
    fun stringToBitmap(imageInString : String) : Bitmap {
        val imageBytes = Base64.decode(imageInString,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return image
    }

}
