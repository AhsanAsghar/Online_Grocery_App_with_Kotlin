package com.example.medicineandgroceryapp

import android.content.Context
import android.graphics.Bitmap

data class DataClassForRequestToDp (val storeImageBitmap : Bitmap, val descriptionOfNotification : String, val context : Context, val phoneOfDeliveryPerson: String, val store_id : String)