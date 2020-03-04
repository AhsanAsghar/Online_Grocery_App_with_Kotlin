package com.example.medicineandgroceryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class CustomDataStoreCategoryButton(val userList: ArrayList<DataClassStoreCategoryButton>) : RecyclerView.Adapter<CustomDataStoreCategoryButton.ViewHolder>() {
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val nameOfButton = itemView.findViewById(R.id.category_buttons) as Button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.button_store_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: DataClassStoreCategoryButton = userList[position]
        holder.nameOfButton.text = user.categoryOfStore
    }

}