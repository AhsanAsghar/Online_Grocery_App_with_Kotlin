package com.example.medicineandgroceryapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ItemsInCategoryViewHolderLabel(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    val label: TextView

    fun setLabel(label: String?) {
        this.label.text = label
    }

    init {
        label = itemView.findViewById<View>(R.id.category_label) as TextView
    }
}
