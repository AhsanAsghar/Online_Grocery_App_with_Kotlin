package com.example.medicineandgroceryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterClassForRequestsOfCustomer(val userList: ArrayList<DataClassForRequestsOfCustomer>) : RecyclerView.Adapter<CustomAdapterClassForRequestsOfCustomer.ViewHolder>() {
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val nameOfCustomer = itemView.findViewById(R.id.customer_name) as TextView
        val customerPhoto = itemView.findViewById(R.id.customer_photo) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.requests_of_customer_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: DataClassForRequestsOfCustomer = userList[position]
        holder.customerPhoto.setImageBitmap(user.photoBitmap)
        holder.nameOfCustomer.text = user.nameOfCustomer
    }

}