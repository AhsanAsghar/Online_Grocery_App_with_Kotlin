package com.example.medicineandgroceryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterClassForNotifications(val userList: ArrayList<DataClassForNotifications>) : RecyclerView.Adapter<CustomAdapterClassForNotifications.ViewHolder>() {
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val descriptionOfNotification = itemView.findViewById(R.id.notification_description) as TextView
        val notificaition_photo = itemView.findViewById(R.id.notification_photo) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.notifications_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: DataClassForNotifications = userList[position]
        holder.notificaition_photo.id = user.idOfPhoto;
        holder.descriptionOfNotification.text = user.descriptionOfNotification
    }

}