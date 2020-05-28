package com.example.medicineandgroceryapp
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_biodata_of_store.*
import java.io.ByteArrayOutputStream
import java.io.InputStream

class biodata_of_store : AppCompatActivity() {
    var phone: String = "null"
    var store_address : String = "null"
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var name: String = "null"
    val PERMISSION_ID = 42
    private var requestCodeP: Int = 999
    var store_type_spinner: String = "null"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biodata_of_store)
        if (intent.getStringExtra("phone") != null && intent.getStringExtra("name") != null) {

        } else {
            phone = "+923004579023"
            name = "Ahsan"
        }

        val store_nameET: EditText = findViewById(R.id.store_name)
        val store_addressET: EditText = findViewById(R.id.store_address)
        val store_emailET: EditText = findViewById(R.id.store_email)
        val imageBitmap: CircleImageView = findViewById(R.id.store_image)
        val next: Button = findViewById(R.id.store_next_button)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

        imageBitmap.setOnClickListener {
            ActivityCompat.requestPermissions(
                this@biodata_of_store,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                requestCodeP
            )
        }
        val storetype = resources.getStringArray(R.array.store_type)

        // access the spinner
        val spinner = findViewById<Spinner>(R.id.storetype_spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, storetype
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        this@biodata_of_store,
                        "selected_item" + " " +
                                "" + storetype[position], Toast.LENGTH_SHORT
                    ).show()
                    store_type_spinner = storetype[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        next.setOnClickListener {
            val store_name = store_nameET.text.toString()
            val store_email = store_emailET.text.toString()
            val queu = Volley.newRequestQueue(applicationContext)
            var url: String =
                "https://grocerymedicineapp.000webhostapp.com/PHPfiles/biodata_of_store.php"
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
                        params.put("name_of_store", store_name.toString())
                        params.put("address_of_store", store_address.toString())
                        params.put("email_address", store_email.toString())
                        params.put("store_type", store_type_spinner)
                        var image: ImageView = findViewById(R.id.store_image)
                        var bitmap: Bitmap = (image.drawable as BitmapDrawable).bitmap
                        var photo: String = bitmapToString(bitmap)
                        params.put("image_of_store", photo)
                        params.put("owner_name", name)
                        return params
                    }
                }
            queu.add(postRequest)
        }
    }



    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        findViewById<TextView>(R.id.store_address).text = location?.longitude.toString() +"," + location?.latitude.toString()
                        store_address =  location?.longitude.toString() +"," + location?.latitude.toString()
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            if(mLastLocation != null){
                findViewById<TextView>(R.id.store_address).text = mLastLocation.longitude.toString() +"," + mLastLocation.latitude.toString()
                store_address =  mLastLocation.longitude.toString() +"," + mLastLocation.latitude.toString()
            }

        }
    }


    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestCode) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                var intent = Intent(Intent.ACTION_PICK)
                intent.setType("image/*")
                startActivityForResult(Intent.createChooser(intent, "Select Image"), requestCode)

            } else {
                Toast.makeText(applicationContext, "Don't have access", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestCodeP && resultCode == Activity.RESULT_OK && data != null) {
            var filepath: Uri = data.data
            var inputStram: InputStream = contentResolver.openInputStream(filepath)
            var bitmap: Bitmap = BitmapFactory.decodeStream(inputStram)
            var nhdouble: Double = (bitmap.height * (512.0 / bitmap.width))
            var nh: Int = nhdouble.toInt()
            var scaled: Bitmap = Bitmap.createScaledBitmap(bitmap, 512, nh, true)
            var photo: CircleImageView = findViewById(R.id.store_image)
            photo.setImageBitmap(scaled)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun bitmapToString(bitmap: Bitmap): String {
        var outputStream: ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        var imageBytes = outputStream.toByteArray()
        var encodedImage: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return encodedImage
    }


/*    private fun checkEmail(email: String): Boolean {
        if (email.contains("@") && !email.equals(null)) {
            return true
        }
        return false*/
    }


