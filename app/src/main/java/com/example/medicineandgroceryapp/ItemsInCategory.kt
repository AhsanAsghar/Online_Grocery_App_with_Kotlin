package com.example.medicineandgroceryapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
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

    var phoneCustomer: String? = null
    var idFromIntent : String? = null
    val displayUser = ArrayList<DataItemsInCategoryParent>()
    var searchUserList = ArrayList<DataItemsInCategoryParent>()
    var adapter : CustomAdapterForItemsInCategory? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_in_category)
        phoneCustomer = intent.getStringExtra("phone")
        idFromIntent = intent.getStringExtra("idOfStore")
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_items_in_category)
        setSupportActionBar(mToolbar)

        //checkIfStoreOwner();
        //chekIfDeliveryPerson();

        val recycleViewOfItemsInCategory = findViewById(R.id.recycler_items_in_category) as RecyclerView
        recycleViewOfItemsInCategory.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        val progress = ProgressBar(this@ItemsInCategory)
        progress.startLoading(true,"Getting Items - Please wait")
        val queue = Volley.newRequestQueue(this)
        val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/ItemsInCategory.php?id=$idFromIntent&"
        val request : StringRequest = StringRequest(url_get, Response.Listener {
                response ->
            Log.d("in",response.toString())
            if(response.toString() == "{\"response\":[null]}"){
                Log.d("in","in")
            }else{
                val jObject : JSONObject = JSONObject(response.toString())
                val jsonArray : JSONArray = jObject?.getJSONArray("response")!!
                Log.d("json",jsonArray.toString())
                val a = jsonArray.length()
                Log.d("json",a.toString())
                val listOfCategoryAdded = mutableListOf<String>()
                for(x in 1..a-1){
                    val pxcategory = jsonArray.getJSONObject(x).getString("product_category")
                    Log.d("list", "x" + x.toString())
                    if(!listOfCategoryAdded.contains(pxcategory)) {
                        displayUser.add(DataClassForDataItemsInCategoryLabel(pxcategory))
                        for(y in 1..a-1){
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
                                displayUser.add(DataClassForDataItemsInCategory(pimage,pname, pprice,this,pid.toInt(),idFromIntent!!,phoneCustomer!!))
                            }
                        }
                        listOfCategoryAdded.add(pxcategory)
                        Log.d("list", listOfCategoryAdded.toString())
                    }
                }
                searchUserList.addAll(displayUser)
                adapter = CustomAdapterForItemsInCategory(displayUser)
                recycleViewOfItemsInCategory.adapter = adapter
                progress.dismissDialog()
            }

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
                    params.put("phone", phoneCustomer!!)
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
                    params.put("phone", phoneCustomer!!)
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


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.cart_items_in_category -> {
                val intent = Intent(this@ItemsInCategory,StoreNameInCart::class.java)
                intent.putExtra("phone",phoneCustomer)
                intent.putExtra("id",idFromIntent)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_items_in_category,menu)
        val searchItem = menu?.findItem(R.id.search_items_in_category)
        if(searchItem != null){
            val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
            val edittext = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            edittext.setHint("Search for items")
            searchView.setOnQueryTextListener( object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText!!.isNotEmpty()){
                        displayUser.clear()
                        val search = newText.toLowerCase()
                        searchUserList.forEach{
                            if(it is DataClassForDataItemsInCategory){
                                val item = it as DataClassForDataItemsInCategory
                                Log.d("search",search)
                                if(item.nameOfItem.toString().toLowerCase().contains(search)){
                                    displayUser.add(it)
                                }
                            }else{
                                val item = it as DataClassForDataItemsInCategoryLabel
                            }


                        }
                        adapter?.notifyDataSetChanged()
                    }else{
                        displayUser.clear()
                        displayUser.addAll(searchUserList)
                        adapter?.notifyDataSetChanged()
                    }
                    return true
                }

            })
        }

        return super.onCreateOptionsMenu(menu)
    }


    fun stringToBitmap(imageInString : String) : Bitmap {
        val imageBytes = Base64.decode(imageInString,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return image
    }

}
