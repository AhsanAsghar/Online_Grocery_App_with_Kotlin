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
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream

class delivery_person_profile : AppCompatActivity() {
    var phone : String? = null
    var requestCodeP: Int = 999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_person_profile)
        if (intent.getStringExtra("phone") != null) {
            phone = intent.getStringExtra("phone")
        } else {
            phone = "+923450694449"
        }
        val dp_nameToEditET: EditText = findViewById(R.id.deliveryperson_name_toedit)
        val dp_bikeNumberET: EditText = findViewById(R.id.deliveryperson_bike_number)
        val dp_licenseET: EditText = findViewById(R.id.deliveryperson_lisence_number)
        val dp_emailAddressET: EditText = findViewById(R.id.deliveryperson_email_address)
        val dp_nameET: TextView = findViewById(R.id.deliveryperson_name)
        val dp_available : SwitchCompat = findViewById(R.id.availability)
        val dp_done: FloatingActionButton = findViewById(R.id.deliveryperson_done)
        val dp_image: de.hdodenhof.circleimageview.CircleImageView =
            findViewById(R.id.deliveryperson_profile_image)
        //Get data from server
        val queue = Volley.newRequestQueue(this)
        var dp_availableNow : Boolean? = null
        val url_img =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/deliveryPersonProfileImg.php?phone=+923450694449"
        val request_img: ImageRequest = ImageRequest(url_img, Response.Listener { response ->
            dp_image.setImageBitmap(response)

        }, 0, 0, null, Response.ErrorListener { error ->
            Log.d("photo", error.toString())

        })
        queue.add(request_img)
        val url_get: String =
            "https://grocerymedicineapp.000webhostapp.com/PHPfiles/deliveryPersonProfileGet.php?phone=$phone"
        val request: StringRequest = StringRequest(url_get, Response.Listener { response ->
            Log.d("json", response.toString())
            //Toast.makeText(this@settings,response.toString(),Toast.LENGTH_SHORT).show()
            //var json : JSONArray = response.getJSONArray(0)
            val jObject: JSONObject = JSONObject(response.toString())
            val jsonArray: JSONArray = jObject?.getJSONArray("response")!!
            val jsonObject: JSONObject = jsonArray.getJSONObject(0);
            val dp_namejson: String = jsonObject.getString("dp_name")
            val dp_emailjson: String = jsonObject.getString("dp_email")
            val dp_bike_number_json: String = jsonObject.getString("bike_number")
            val dp_license_number_json: String = jsonObject.getString("lisence_number")
            val dp_available_json: String = jsonObject.getString("available")
            if (dp_namejson != "null") {
                dp_nameET.text = dp_namejson
                dp_nameToEditET.setText(dp_namejson)
            }
            if (dp_emailjson != "null")
                dp_emailAddressET.setText(dp_emailjson)
            if (dp_bike_number_json != "null")
                dp_bikeNumberET.setText(dp_bike_number_json)
            if (dp_license_number_json != "null")
                dp_licenseET.setText(dp_license_number_json)
            if(dp_available_json == "1"){
                dp_available.isChecked = true
                dp_availableNow = true
            }else{
                dp_available.isChecked = false
                dp_availableNow = false
            }
            Log.d("avail", dp_available_json)

        }, Response.ErrorListener { error ->
            Log.d("json", error.toString())
            Toast.makeText(this@delivery_person_profile, error.toString(), Toast.LENGTH_SHORT)
                .show()
        })
        queue.add((request))
        // End getting
        val dp_nameNow = dp_nameToEditET.text.toString()
        val dp_bikeNumberNow = dp_bikeNumberET.text.toString()
        val dp_licenseNumberNow = dp_licenseET.text.toString()
        val dp_emailAddressNow = dp_emailAddressET.text.toString()
        var imageBitmap: Bitmap = (dp_image.drawable as BitmapDrawable).bitmap
        var dp_available_in: Boolean? = dp_availableNow
        dp_available.setOnCheckedChangeListener(){
                buttonView, isChecked ->
            Log.d("avail",isChecked.toString())
            dp_available_in = isChecked
            Log.d("avail", dp_available_in.toString())
        }
        dp_image.setOnClickListener() { v ->
            ActivityCompat.requestPermissions(
                this@delivery_person_profile,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                requestCodeP
            )
        }
        dp_done.setOnClickListener() { v ->
            Log.d("avail",dp_availableNow.toString())
            var imageForBitmapTest: ImageView = findViewById(R.id.deliveryperson_profile_image)
            var imageBitmapTest: Bitmap = (dp_image.drawable as BitmapDrawable).bitmap
            if (!checkBikeNumber(dp_bikeNumberET.text.toString())) {
                dp_bikeNumberET.error = "Add correct Bike Number"
            } else if(!checkEmail(dp_emailAddressET.text.toString()))
                dp_emailAddressET.error = "Enter Correct Email"
            else if(!checkLicenseNumber(dp_licenseET.text.toString()))
                dp_licenseET.error = "Enter correct license Number"
            else if(dp_nameToEditET.text.toString() != dp_nameNow || dp_bikeNumberET.text.toString() != dp_bikeNumberNow
                || dp_licenseET.text.toString() != dp_licenseNumberNow || dp_emailAddressET.text.toString() != dp_emailAddressNow
                || imageBitmap != imageBitmapTest || dp_available_in != dp_availableNow){
                Log.d("Error", "inside If")
                val queu = Volley.newRequestQueue(applicationContext)
                var url: String =
                    "https://grocerymedicineapp.000webhostapp.com/PHPfiles/deliveryPersonProfile.php"
                val postRequest =
                    object : StringRequest(Request.Method.POST, url, Response.Listener { response ->
                        Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }, Response.ErrorListener { error ->
                        Log.d("Error", error.toString())
                        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG)
                            .show()
                    }) {
                        override fun getParams(): Map<String, String> {
                            val params = HashMap<String, String>()
                            params.put("name", dp_nameToEditET.text.toString())
                            params.put("bike_number", dp_bikeNumberET.text.toString())
                            params.put("phone", (phone.toString()))
                            params.put("license_number",dp_licenseET.text.toString())
                            params.put("email_address",dp_emailAddressET.text.toString())
                            if(dp_available_in == true){
                                Log.d("avail",dp_available_in.toString())
                                params.put("available","true")
                            }
                            else if (dp_available_in == false){
                                Log.d("avail",dp_available_in.toString())
                                params.put("available","false")
                            }else{
                                Log.d("avail","its null")
                            }
                            var image: ImageView = findViewById(R.id.deliveryperson_profile_image)
                            var bitmap: Bitmap = (image.drawable as BitmapDrawable).bitmap
                            var photo: String = bitmapToString(bitmap)
                            params.put("image", photo)
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
            var photo : ImageView = findViewById(R.id.deliveryperson_profile_image)
            photo.setImageBitmap(scaled)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun checkEmail(email : String) : Boolean{
        if(email.contains("@") && !email.equals(null) && email.contains(".com")){
            return true
        }
        return false
    }
    private fun checkBikeNumber(bikeNumber: String) : Boolean{
        val pattern1 = "[a-zA-Z] [1-9]".toRegex()
        val pattern2 = "[a-zA-Z][1-9]".toRegex()
        val match1 = pattern1.find(bikeNumber)
        val match2 = pattern2.find(bikeNumber)
        val id1 = match1?.value
        val id2= match2?.value
        if((!id1.equals(null) || !id2.equals(null)) && bikeNumber.length >= 5) {
            return true
        }
        return false
    }
    private fun checkLicenseNumber(licenseNumber : String) : Boolean{
        val pattern = "[1-9]".toRegex()
        val match = pattern.find(licenseNumber)
        val id1 = match?.value
        if(id1 != null && !licenseNumber.equals(null)){
            return true
        }
        return false
    }
    private fun bitmapToString(bitmap: Bitmap) : String {
        var outputStream : ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        var imageBytes = outputStream.toByteArray()
        var encodedImage : String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return encodedImage
    }
}
