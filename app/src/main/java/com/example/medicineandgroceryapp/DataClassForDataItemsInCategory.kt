package com.example.medicineandgroceryapp

import android.graphics.Bitmap

class DataClassForDataItemsInCategory(val idOfImage: Bitmap, val nameOfItem : String, val priceOfItem : String):
DataItemsInCategoryParent(DataItemsInCategoryParent.TYPE.DATA){
}