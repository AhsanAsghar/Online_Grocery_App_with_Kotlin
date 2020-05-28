package com.example.medicineandgroceryapp

import android.content.Context
import android.graphics.Bitmap

class DataClassForItemsInStoreProfile(val idOfImageStoreProfileItems :Bitmap, val nameOfStoreProfileItem : String, val priceOfStoreProfileItems : String, val id : String, val context : Context): DataItemsInStoreProfileAbstract(DataItemsInStoreProfileAbstract.TYPE.DATA) {
}