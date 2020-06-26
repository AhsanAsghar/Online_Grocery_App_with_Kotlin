package com.example.medicineandgroceryapp

import android.content.Context
import android.graphics.Bitmap

class DataClassForDataItemsInCategory(val idOfImage: Bitmap, val nameOfItem : String, val priceOfItem : String, val context:Context, val pid: Int, val idOfStore: Int,val customerNumber:String):
DataItemsInCategoryParent(DataItemsInCategoryParent.TYPE.DATA){
}