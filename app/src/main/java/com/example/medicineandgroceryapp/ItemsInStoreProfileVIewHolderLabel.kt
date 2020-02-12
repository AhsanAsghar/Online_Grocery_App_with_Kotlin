package com.example.medicineandgroceryapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemsInStoreProfileVIewHolderLabel(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    val label: TextView

    fun setLabel(label: String?) {
        this.label.text = label
    }

    init {
        label = itemView.findViewById<View>(R.id.label_items_in_store_profile) as TextView
    }
}