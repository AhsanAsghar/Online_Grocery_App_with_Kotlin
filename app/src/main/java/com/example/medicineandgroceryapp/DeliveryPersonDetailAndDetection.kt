package com.example.medicineandgroceryapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class DeliveryPersonDetailAndDetection : AppCompatActivity() {
    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_person_detail_and_detection)
        val infoButton = findViewById(R.id.info) as Button
        infoButton.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.map_dialogue,null)
            val cnic = dialogView.findViewById<TextView>(R.id.cnic)
            val phoneNumber = dialogView.findViewById<TextView>(R.id.phone_no)
            dialog.setView(dialogView)
            dialogView.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            dialog.show()
        }
        mapFragment = supportFragmentManager.findFragmentById(R.id.fragment_delivery_person_detail_and_detection) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
        })
    }
}
