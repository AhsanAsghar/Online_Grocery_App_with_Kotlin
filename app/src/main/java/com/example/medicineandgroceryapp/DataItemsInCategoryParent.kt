package com.example.medicineandgroceryapp

abstract class DataItemsInCategoryParent(val type: String){
    class TYPE{
        companion object{
            val LABEL = "label"
            val DATA = "data"
        }
    }
}