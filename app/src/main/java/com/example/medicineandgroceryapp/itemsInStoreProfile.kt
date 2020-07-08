package com.example.medicineandgroceryapp


import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class itemsInStoreProfile : AppCompatActivity() {
    val CAMERA_REQUEST_CODE : Int = 2
    val CAMERA_CODE : Int = 0
    val REQUEST_CODE_FOR_GALLERY : Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_in_store_profile)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_store_profile_items)
        setSupportActionBar(mToolbar)
        val phone : String = intent.getStringExtra("phone")
        val nameOfStore : TextView = findViewById(R.id.name_of_store)
        //Get Name of store
        val queue = Volley.newRequestQueue(this)
        val url_get_name : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/itemsInStoreProfileNameGet.php?phone=$phone"
        val request_name : StringRequest = StringRequest(url_get_name, Response.Listener {
                response ->
            val store  = response.toString().split(":").toTypedArray()
            val storeName = store[1].substring(1,store[1].length - 2)
            findViewById<TextView>(R.id.name_of_store).setText(storeName)

        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@itemsInStoreProfile,error.toString(),Toast.LENGTH_SHORT).show()
        })
        queue.add((request_name))
        //End getting name of store
        val recycleViewOfItemsInCategory = findViewById(R.id.recycler_store_profile_items) as RecyclerView
        recycleViewOfItemsInCategory.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        val resid : Int = R.id.item_photo_in_store_profile_items;
        val users = ArrayList<DataItemsInStoreProfileAbstract>()
        val storeProfileImage: ImageView = findViewById(R.id.store_profile_image)
        val floatingButton : FloatingActionButton = findViewById(R.id.floatingActionButtonStoreProfile)
        floatingButton.setOnClickListener(){
            v ->
            getAlertBar()
        }
        val url_img = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/itemsInStoreProfileImageGet.php?phone=$phone"
        val request_img : ImageRequest = ImageRequest(url_img, Response.Listener {
                response ->
            storeProfileImage.setImageBitmap(response)

        },0,0,null, Response.ErrorListener {
                error ->
            Log.d("photo",error.toString())

        } )
        queue.add(request_img)

        val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/itemsInStoreProfileImageOfItemsGet.php?phone=$phone"
        val request : StringRequest = StringRequest(url_get, Response.Listener {
                response ->
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
                    users.add(DataClassForItemsInStoreProfileLabel(pxcategory))
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
                            users.add(DataClassForItemsInStoreProfile(pimage,pname, pprice,pid,this))
                        }
                    }
                    listOfCategoryAdded.add(pxcategory)
                    Log.d("list", listOfCategoryAdded.toString())
                }
            }
            val adapter = CustomAdapterForItemsInStoreProfile(users)
            recycleViewOfItemsInCategory.adapter = adapter
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@itemsInStoreProfile,error.toString(),Toast.LENGTH_SHORT).show()
        })
        queue.add((request))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_CODE_FOR_GALLERY){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK)
                intent.setType("image/*")
                startActivityForResult(Intent.createChooser(intent,"Select Image"),requestCode)

            }else{
                Toast.makeText(applicationContext,"Don't have access",Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getAlertBar() {
        val items: Array<CharSequence> = arrayOf("Camera","Gallery","Close")
        val builder  = AlertDialog.Builder(this@itemsInStoreProfile)
        builder.setTitle("Add picture for new item")
        builder.setItems(items,DialogInterface.OnClickListener{
            dialog, which ->
            if(items[which].equals("Camera")){
                Toast.makeText(applicationContext,"Camera",Toast.LENGTH_SHORT).show()
                callCamera()
            } else if(items[which].equals("Gallery")){
                Toast.makeText(applicationContext,"Gallery",Toast.LENGTH_SHORT).show()
                goIntoGallery()
            } else if(items[which].equals("Close")){
                dialog.dismiss()
            }
        })
        builder.show()
    }

    private fun goIntoGallery() {
        ActivityCompat.requestPermissions(
            this@itemsInStoreProfile,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE_FOR_GALLERY
        )
    }

    private fun callCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(ContextCompat.checkSelfPermission(this@itemsInStoreProfile, android.Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_GRANTED){
            startActivityForResult(cameraIntent,CAMERA_CODE)
        }else{
            ActivityCompat.requestPermissions(this@itemsInStoreProfile, arrayOf(android.Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == CAMERA_CODE){
            if(data != null){
                var image = data.extras.get("data") as Bitmap
                val intent = Intent(this@itemsInStoreProfile,itemsDetailFromCamera::class.java)
                intent.putExtra("image",bitmapToString(image))
                intent.putExtra("type",true)
                startActivity(intent)
            }

        }else if (requestCode == REQUEST_CODE_FOR_GALLERY){
            if(data != null){
                val selectedimage : Uri = data.data
                val intent = Intent(this@itemsInStoreProfile,itemsDetailFromCamera::class.java)
                intent.putExtra("image",selectedimage.toString())
                intent.putExtra("type",false)
                startActivity(intent)
            }
        } else{
            Toast.makeText(applicationContext,requestCode.toString(),Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
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
                Toast.makeText(applicationContext,"looking for $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(applicationContext,"looking fotr $newText", Toast.LENGTH_SHORT).show()
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
    fun bitmapToString(bitmap: Bitmap) : String {
        var outputStream : ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        var imageBytes = outputStream.toByteArray()
        var encodedImage : String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return encodedImage
    }
    fun stringToBitmap(imageInString : String) : Bitmap{
        val imageBytes = Base64.decode(imageInString,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return image
    }
}
