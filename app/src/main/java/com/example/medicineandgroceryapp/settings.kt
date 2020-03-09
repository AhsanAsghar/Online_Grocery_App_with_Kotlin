package com.example.medicineandgroceryapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream

class settings : AppCompatActivity() {
    private var requestCodeP: Int = 999
    private var phone: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if(intent.getStringExtra("phone") != null){
            Log.d("Error","inside")
        }
        else{
            phone = "+923450694449"
        }
        val card_setting : CardView = findViewById(R.id.cardView_setting)
        val imageForBitmap : ImageView = findViewById(R.id.profilePic)
        val imageBitmap : Bitmap = (imageForBitmap.drawable as BitmapDrawable).bitmap
        val name : TextView = findViewById(R.id.nameUnderProfilePic)
        val nameToEdit : EditText = findViewById(R.id.nameToEdit)
        val password : EditText = findViewById(R.id.password_setting)
        val done : FloatingActionButton = findViewById(R.id.floatingActionButtonAccept)
        val nameToEditText : String = nameToEdit.text.toString()
        val passwordNow : String = password.text.toString()
        //Get data from server
        val queue = Volley.newRequestQueue(this)
        val url_img = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/settings_img.php?phone=+923450694449"
        val request_img : ImageRequest = ImageRequest(url_img, Response.Listener {
            response ->
            imageForBitmap.setImageBitmap(response)

        },0,0,null,Response.ErrorListener {
            error ->
            Log.d("photo",error.toString())

        } )
        queue.add(request_img)
        val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/settings_get.php?phone=$phone"
        val request : StringRequest = StringRequest(url_get,Response.Listener {
            response ->
            Log.d("json",response.toString())
            //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
            //var json : JSONArray = response.getJSONArray(0)
            val jObject : JSONObject = JSONObject(response.toString())
            val jsonArray :JSONArray= jObject?.getJSONArray("response")!!
            val jsonObject : JSONObject = jsonArray.getJSONObject(0);
            val namejson : String = jsonObject.getString("name")
            val passwordjson : String = jsonObject.getString("password")
            name.setText(namejson)
            nameToEdit.setText(namejson)
            password.setText(passwordjson)
        },Response.ErrorListener {
            error ->
                Log.d("json", error.toString())
                Toast.makeText(this@settings,error.toString(),Toast.LENGTH_SHORT).show()
        })
        queue.add((request))
        // End getting
        card_setting.setOnClickListener(){
            v ->
            ActivityCompat.requestPermissions(
                this@settings,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                requestCodeP
            )
        }
        done.setOnClickListener(){
            v ->
            var imageForBitmapTest : ImageView = findViewById(R.id.profilePic)
            if(password.text.toString().length != 8){
                Toast.makeText(this@settings, "Enter 8 characters in password", Toast.LENGTH_SHORT).show()
            }
            var imageBitmapTest : Bitmap = (imageForBitmap.drawable as BitmapDrawable).bitmap
            if((passwordNow != password.text.toString() || nameToEditText != nameToEdit.text.toString() ||
                    imageBitmap != imageBitmapTest) && password.text.toString().length == 8){
                Log.d("Error","inside If")
                val queu = Volley.newRequestQueue(applicationContext)
                var url : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/settings.php"
                val postRequest =object: StringRequest(Request.Method.POST,url, Response.Listener {
                        response ->
                    Toast.makeText(applicationContext,response.toString(),Toast.LENGTH_SHORT).show()
                },Response.ErrorListener {error ->
                    Log.d("Error",error.toString())
                    Toast.makeText(applicationContext,error.toString(),Toast.LENGTH_LONG).show()
                }){
                    override fun getParams() : Map<String,String>{
                        val params = HashMap<String,String>()
                        params.put("pass",password.text.toString())
                        params.put("name",nameToEdit.text.toString())
                        params.put("phone",(phone.toString()))
                        var image : ImageView = findViewById(R.id.profilePic)
                        var bitmap : Bitmap = (image.drawable as BitmapDrawable).bitmap
                        var photo : String = bitmapToString(bitmap)
                        params.put("image",photo)
                        return params
                    }
                }
                queu.add(postRequest)
        }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == requestCode){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                var intent = Intent(Intent.ACTION_PICK)
                intent.setType("image/*")
                startActivityForResult(Intent.createChooser(intent,"Select Image"),requestCode)

            }else{
                Toast.makeText(applicationContext,"Don't have access",Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == requestCodeP && resultCode == Activity.RESULT_OK && data != null){
            var filepath : Uri = data.data
            var inputStram : InputStream = contentResolver.openInputStream(filepath)
            var bitmap : Bitmap = BitmapFactory.decodeStream(inputStram)
            var nhdouble: Double = (bitmap.height*(512.0/bitmap.width))
            var nh : Int = nhdouble.toInt()
            var scaled : Bitmap = Bitmap.createScaledBitmap(bitmap,512,nh,true)
            var photo : ImageView = findViewById(R.id.profilePic)
            photo.setImageBitmap(scaled)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun bitmapToString(bitmap: Bitmap) : String {
        var outputStream : ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        var imageBytes = outputStream.toByteArray()
        var encodedImage : String = Base64.encodeToString(imageBytes,Base64.DEFAULT)
        return encodedImage
    }
}
