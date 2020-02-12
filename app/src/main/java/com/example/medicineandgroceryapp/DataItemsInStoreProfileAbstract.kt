package com.example.medicineandgroceryapp

abstract class DataItemsInStoreProfileAbstract(val type: String){
    class TYPE{
        companion object{
            val LABEL = "label"
            val DATA = "data"
        }
    }
}