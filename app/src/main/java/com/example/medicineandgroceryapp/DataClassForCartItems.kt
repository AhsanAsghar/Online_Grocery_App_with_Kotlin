package com.example.medicineandgroceryapp

import android.content.Context
import android.graphics.Bitmap

data class DataClassForCartItems (val photoBitmap : Bitmap, val nameOfItem : String, val priceOfItem : String,val cartId : String, val context : Context,
val store_id: String, val customerPhone : String)