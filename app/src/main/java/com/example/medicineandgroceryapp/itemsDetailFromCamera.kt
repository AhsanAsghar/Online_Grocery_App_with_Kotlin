package com.example.medicineandgroceryapp


import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream
import java.io.InputStream

class itemsDetailFromCamera : AppCompatActivity() {
    val CAMERA_CODE1 = 1
    val CAMERA_REQUEST_CODE1 = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_detail_from_camera)
        val imageToShow: ImageView = findViewById(R.id.itemDetailPic)
        val takeImageAgain: ImageView = findViewById(R.id.takeImageAgain)
        val itemName: EditText = findViewById(R.id.nameOfProductCamera)
        val itemPrice: EditText = findViewById(R.id.priceOfItemCamera)
        val text: TextView = findViewById(R.id.error)
        takeImageAgain.setOnClickListener() { v ->
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (ContextCompat.checkSelfPermission(
                    this@itemsDetailFromCamera,
                    android.Manifest.permission.CAMERA
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                startActivityForResult(cameraIntent, CAMERA_CODE1)
            } else {
                ActivityCompat.requestPermissions(
                    this@itemsDetailFromCamera,
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE1
                )
            }
        }
        if (intent.extras.getBoolean("type")) {
            imageToShow.setImageBitmap(stringToBitmap(intent.getStringExtra("image")))
        } else if (!intent.extras.getBoolean("type")) {
            val uri: Uri = Uri.parse(intent.getStringExtra("image"))
            val inputStram: InputStream = contentResolver.openInputStream(uri)
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStram)
            imageToShow.setImageBitmap(bitmap)
        }
        val phone : String = intent.getStringExtra("phone")
        val typeSpinner: Spinner = findViewById(R.id.itemTypeSpinner)
        val options_bakery = arrayOf("Breads","Doughnuts","Bagels","Cakes and Pies","Pastries",
        "Biscuits","Brownie","Cookies", "Snacks & Candy", "Dairy, Eggs & Cheese")
        val category_grocery = arrayOf("Category of Item",

            "Bread & Bakery",
                "Breakfast & Cereal",
        "Canned Goods & Soups",
        "Deli & Signature Cafe",
        "Flowers",
        "Frozen Foods",
                "Produce: Fruits & Vegetables",
        "Grains, Pasta & Sides",
        "International Cuisine",
                "Meat & Seafood",
        "Miscellaneous – gift cards/wrap, batteries, etc.",
        "Paper Products – toilet paper, paper towels, tissues, paper plates/cups, etc.",
        "Cleaning Supplies – laundry detergent, dishwashing soap, etc.",
        "Health & Beauty, Personal Care & Pharmacy – pharmacy items, shampoo, toothpaste",
        "Pet Care",
                "Pharmacy",
        "Tobacco"
        )
        //Get type of store
        val queue = Volley.newRequestQueue(this)
        val url_store_number : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/getTypeOfStore.php?phone=$phone"
        val request_store_number : StringRequest = StringRequest(url_store_number, Response.Listener {
                response ->
            val result  = response.toString().split(":").toTypedArray()
            val store_type = result[1].substring(1,result[1].length - 2)
            if(store_type.equals("NO")){
                Toast.makeText(this@itemsDetailFromCamera,"Problem in Query",Toast.LENGTH_SHORT).show()
            } else if (store_type.equals("Grocery Store")){
                typeSpinner.adapter =
                    ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, category_grocery)
            } else if (store_type.equals("Bakery Store")){
                typeSpinner.adapter =
                    ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options_bakery)
            }
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@itemsDetailFromCamera,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request_store_number))
        //End getting type of store

        val floatingbuttonCameraActivity: FloatingActionButton =
            findViewById(R.id.floatingActionButtonAccept)
        floatingbuttonCameraActivity.setOnClickListener() { v ->
            if (itemName.text.toString() == "") {
                itemName.setError("Enter a name for item")
            } else if (itemPrice.text.toString() == "") {
                itemPrice.setError("Enter a price for item")
            } else if (typeSpinner.selectedItem == "Category of Item") {
                Toast.makeText(applicationContext, "Please select a category", Toast.LENGTH_SHORT)
                    .show()
                text.setText("Please select a catagory")
                typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        text.setText("")
                    }

                }
            } else{
                val progress = ProgressBar(this@itemsDetailFromCamera)
                progress.startLoading(true,"Storing Items - Please Wait")
                val queu = Volley.newRequestQueue(applicationContext)
                val url: String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/itemsDetailFromCamera.php"
                val postRequest =
                    object : StringRequest(Request.Method.POST, url, Response.Listener { response ->
                        progress.dismissDialog()
                        Log.d("response", response.toString())
                        val result  = response.toString().split(":").toTypedArray()
                        val yesORno = result[1].substring(1,result[1].length - 2)
                        Log.d("piq",yesORno)
                        if(yesORno.equals("YES")){
                            val intent = Intent(this@itemsDetailFromCamera,itemsInStoreProfile::class.java)
                            intent.putExtra("phone",phone)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            this@itemsDetailFromCamera.finish()
                        }else{
                            Toast.makeText(this,"Unable to store - Please Try again",Toast.LENGTH_SHORT).show()
                        }
                    }, Response.ErrorListener { error ->
                        Log.d("Error", error.toString())
                        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
                    }) {
                        override fun getParams(): Map<String, String> {
                            val params = HashMap<String, String>()
                            params.put("phone",phone)
                            params.put("name",itemName.text.toString())
                            params.put("price", itemPrice.text.toString())
                            params.put("category", typeSpinner.selectedItem.toString())
                            val bitmap : Bitmap = (imageToShow.drawable as BitmapDrawable).bitmap
                            val photo : String = bitmapToString(bitmap)
                            params.put("image",photo)
                            return params
                        }
                    }
                queu.add(postRequest)
            }


        }
            }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == CAMERA_CODE1){
            if(data != null){
                val image= data.extras.get("data") as Bitmap
                val imageToChange: ImageView = findViewById(R.id.itemDetailPic)
                imageToChange.setImageBitmap(image)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun bitmapToString(bitmap: Bitmap) : String {
        val outputStream : ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val imageBytes = outputStream.toByteArray()
        val encodedImage : String = Base64.encodeToString(imageBytes,Base64.DEFAULT)
        return encodedImage
    }
    fun stringToBitmap(image1 : String) : Bitmap{
        val imageBytes = Base64.decode(image1, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return image
    }
}
