package com.example.medicineandgroceryapp

import android.content.Context
import android.graphics.Bitmap

data class DataClassForRequestsOfCustomer (val photoBitmap : Bitmap, val nameOfCustomer : String, val customer_phone : String, val context: Context,val store_owner_phone:String)