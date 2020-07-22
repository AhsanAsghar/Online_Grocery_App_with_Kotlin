package com.example.medicineandgroceryapp

import android.content.Intent
import android.graphics.Bitmap
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
        if(user.photoBitmap != null){
            holder.customerPhoto.setImageBitmap(user.photoBitmap)
        }
        holder.nameOfCustomer.text = user.nameOfCustomer
        holder.itemView.setOnClickListener {
                v ->
            val intent = Intent(user.context,RequestDetail::class.java)
            intent.putExtra("phone",user.customer_phone)
            intent.putExtra("store_id",user.storeId)
            user.context.startActivity(intent)
        }
    }

}