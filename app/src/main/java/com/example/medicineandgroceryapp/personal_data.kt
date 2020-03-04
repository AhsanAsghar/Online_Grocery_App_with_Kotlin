package com.example.medicineandgroceryapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*


class personal_data : AppCompatActivity() {
    /*var PERMISSION_ID : Int = 2
    lateinit var mFusedLocationClient : FusedLocationProviderClient
    var latitude: Double = 0.0
    var longitude: Double = 0.0*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data)
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key),Locale.US);
        }
        //getLastLocation()
        val address : EditText = findViewById(R.id.password)
        address.setOnClickListener { view ->
            val fields: List<Place.Field> =
                Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
            val intent: Intent  = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            ).setCountry("PK")
                .build(this)
            //var AUTOCOMPLETE_REQUEST_CODE : Int = 5
            startActivityForResult(intent,5)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 5){
            if(resultCode == Activity.RESULT_OK){
                val place : Place = Autocomplete.getPlaceFromIntent(data!!)
                Toast.makeText(this,place.address,Toast.LENGTH_SHORT ).show()
            } else if(resultCode == AutocompleteActivity.RESULT_ERROR){
                Toast.makeText(this,"failed to get",Toast.LENGTH_SHORT ).show()
            } else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"result cancel",Toast.LENGTH_SHORT ).show()
            }
        }
    }

    /*private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }
    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun getLastLocation() {
        Log.d("DEBUG","1")
        if (checkPermissions()) {
            Log.d("DEBUG","2 - have permission")
            if (isLocationEnabled()) {
                Log.d("DEBUG","3 - location enable")

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        Log.d("DEBUG","4-null")
                        requestNewLocationData()
                        Toast.makeText(this, latitude.toString() + longitude.toString(),Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("DEBUG","4 - not null")
                        latitude = location.latitude
                        longitude = location.longitude
                        Toast.makeText(this, latitude.toString() + longitude.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Log.d("DEBUG","3-location disable")
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        Log.d("DEBUG","4-null")
                        while(latitude == 0.0 && longitude == 0.0)
                        requestNewLocationData()
                        Toast.makeText(this, latitude.toString() + longitude.toString(),Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("DEBUG","4 - not null")
                        latitude = location.latitude
                        longitude = location.longitude
                        Toast.makeText(this, latitude.toString() + longitude.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Log.d("DEBUG","2 - no permission")
            requestPermissions()
        }
    }
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
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
        }
    }*/

}

