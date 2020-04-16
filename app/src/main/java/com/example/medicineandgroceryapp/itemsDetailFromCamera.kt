package com.example.medicineandgroceryapp


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
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.InputStream

class itemsDetailFromCamera : AppCompatActivity() {
    val CAMERA_CODE1 = 1
    val CAMERA_REQUEST_CODE1 = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_detail_from_camera)
        val imageToShow : ImageView = findViewById(R.id.itemDetailPic)
        val default = imageToShow.drawable
        val takeImageAgain: ImageView = findViewById(R.id.takeImageAgain)
        takeImageAgain.setOnClickListener(){
            v ->
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(ContextCompat.checkSelfPermission(this@itemsDetailFromCamera, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED){
                startActivityForResult(cameraIntent,CAMERA_CODE1)
            }else{
                ActivityCompat.requestPermissions(this@itemsDetailFromCamera, arrayOf(android.Manifest.permission.CAMERA),CAMERA_REQUEST_CODE1)
            }
        }
        if(intent.extras.getBoolean("type")){
            imageToShow.setImageBitmap(stringToBitmap(intent.getStringExtra("image")))
        } else if(!intent.extras.getBoolean("type")){
            val uri : Uri = Uri.parse(intent.getStringExtra("image"))
            val inputStram : InputStream = contentResolver.openInputStream(uri)
            val bitmap : Bitmap = BitmapFactory.decodeStream(inputStram)
            imageToShow.setImageBitmap(bitmap)
        }
        val typeSpinner : Spinner = findViewById(R.id.itemTypeSpinner)
        val options = arrayOf("Category of Item","option 1","option 2", "option 3")
        typeSpinner.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)
        typeSpinner.onItemSelectedListener =  object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
    fun stringToBitmap(imageInString : String) : Bitmap{
        val imageBytes = Base64.decode(imageInString,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return image
    }
}
