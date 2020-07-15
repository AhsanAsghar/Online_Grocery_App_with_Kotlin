package com.example.medicineandgroceryapp

import android.content.Context
import android.graphics.Bitmap

data class DataClassForNearbyStores(val idOfPhoto: Bitmap, val nameOfStore: String, val distance: String, val storePhoneStore:String, val context:Context, val idOfStore: String)