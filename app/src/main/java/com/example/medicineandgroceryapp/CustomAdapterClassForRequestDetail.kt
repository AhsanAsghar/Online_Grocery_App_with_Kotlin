package com.example.medicineandgroceryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterClassForRequestDetail(val userList: ArrayList<DataClassForRequestDetails>) : RecyclerView.Adapter<CustomAdapterClassForRequestDetail.ViewHolder>() {
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val nameOfitemRequestDetail = itemView.findViewById(R.id.cart_item_name_request_detail) as TextView
        val itemPhotoRequestDetail = itemView.findViewById(R.id.cart_item_photo_request_detail) as ImageView
        val priceOfItemRequestDetail = itemView.findViewById(R.id.price_request_detail) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.request_detail_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: DataClassForRequestDetails = userList[position]
        holder.itemPhotoRequestDetail.id = user.idOfPhotoRequestDetail
        holder.nameOfitemRequestDetail.text = user.nameOfItemRequestDetail
        holder.priceOfItemRequestDetail.text = user.priceOfItemRequestDetail
    }

}