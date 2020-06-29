package com.example.medicineandgroceryapp
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView


class NearestStores : AppCompatActivity() {
    var phone: String = "null"
    var name :String = "null"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearest_stores)
        if (intent.getStringExtra("phone") != null && intent.getStringExtra("name") != null) {

        } else {
            phone = "+923004579023"
            name = "Ahsan"
        }
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar2)
        setSupportActionBar(mToolbar)
       /* val storeOwner = checkIfStoreOwner();
        val deliveryPerson =chekIfDeliveryPerson();

        if (storeOwner.equals('1') && deliveryPerson.equals('0')){


            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navView: NavigationView = findViewById(R.id.nav_view)
            val toggle = ActionBarDrawerToggle(
                this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            navView.setNavigationItemSelectedListener(this)





        }
        else if(storeOwner.equals('0') && deliveryPerson.equals('1')){

        }

        else{

        }


*/

        val recycle = findViewById(R.id.recyclerView) as RecyclerView
        val recycleButton = findViewById(R.id.recyclerView) as RecyclerView
        val spinner = findViewById<Spinner>(R.id.spinner)
        val grocormedic = arrayOf("Grocery Store", "Medical Store")
        spinner.adapter = ArrayAdapter<String> (this, android.R.layout.simple_expandable_list_item_1,grocormedic)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position == 0){

                    Toast.makeText(applicationContext , "Grocery", Toast.LENGTH_SHORT).show()
                }
                else if(position == 1){
                    Toast.makeText(applicationContext , "Medical", Toast.LENGTH_SHORT).show()
                }
            }


        }
        recycle.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        recycleButton.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val storeCategory = ArrayList<DataClassStoreCategoryButton>()
        storeCategory.add(DataClassStoreCategoryButton("Grocery Store"))
        storeCategory.add(DataClassStoreCategoryButton("Bakery Store"))
        storeCategory.add(DataClassStoreCategoryButton("Pharmacy"))
        storeCategory.add(DataClassStoreCategoryButton("General Store"))
        val buttonAdapter = CustomDataStoreCategoryButton(storeCategory)
        recycleButton.adapter = buttonAdapter
        val users = ArrayList<DataClassForNearbyStores> ()
        val resid = R.drawable.store
        users.add(DataClassForNearbyStores(resid,"Store name","3 km"))
        users.add(DataClassForNearbyStores(resid,"Store name","3 km"))
        users.add(DataClassForNearbyStores(resid,"Store name","3 km"))
        users.add(DataClassForNearbyStores(resid,"Store name","3 km"))

        val adapter = CustomAdapterForNearbyStores(users)
        recycle.adapter = adapter
    }

//    private fun chekIfDeliveryPerson(): StringRequest {
//        val queu = Volley.newRequestQueue(applicationContext)
//        var url: String =
//            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkIfDeliveryPerson.php"
//        val postRequest =
//            object : StringRequest(Request.Method.POST, url, Response.Listener { response ->
//                Log.d("response", response.toString())
//                Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT)
//                    .show()
//            }, Response.ErrorListener { error ->
//                Log.d("error", error.toString())
//                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT)
//                    .show()
//            }) {
//                override fun getParams(): Map<String, String> {
//                    val params = HashMap<String, String>()
//                    params.put("phone", phone)
//                    return params
//                }
//            }
//        queu.add(postRequest)
//
//
//        var queue: RequestQueue
//        queue = Volley.newRequestQueue(this)
//        val URL: String =  "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkIfDeliveryPerson.php"
//        val request = StringRequest(
//            Request.Method.GET,
//            URL,
//            Response.Listener { response ->
//               val deliveryPerson = response;
//            },
//            Response.ErrorListener { error -> Log.d("error", error.toString()) })
//        return request
//        queue.add(request)
//
//    }
//
//    private fun checkIfStoreOwner(): StringRequest {
//        val queu = Volley.newRequestQueue(applicationContext)
//        var url: String =
//            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkIfStoreOwner.php"
//        val postRequest =
//            object : StringRequest(Request.Method.POST, url, Response.Listener { response ->
//                Log.d("response", response.toString())
//                Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT)
//                    .show()
//            }, Response.ErrorListener { error ->
//                Log.d("error", error.toString())
//                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT)
//                    .show()
//            }) {
//                override fun getParams(): Map<String, String> {
//                    val params = HashMap<String, String>()
//                    params.put("phone", phone)
//                    return params
//                }
//            }
//        queu.add(postRequest)
//
//
//
//        var queue: RequestQueue
//        queue = Volley.newRequestQueue(this)
//        val URL: String =  "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkIfStoreOwner.php"
//        val request = StringRequest(
//            Request.Method.GET,
//            URL,
//            Response.Listener { response ->
//                val storeOwner = response;
//            },
//            Response.ErrorListener { error -> Log.d("error", error.toString()) })
//        return request
//        queue.add(request)
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nearest_stores,menu)
        return super.onCreateOptionsMenu(menu)
    }
}


