package com.example.medicineandgroceryapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterClassForStoreNameInCart(val userList: ArrayList<DataClassStoreNameInCart>) : RecyclerView.Adapter<CustomAdapterClassForStoreNameInCart.ViewHolder>() {
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val nameOfStoreInCart = itemView.findViewById(R.id.store_name_in_cart) as TextView
        val storePhoto = itemView.findViewById(R.id.store_photo) as ImageView
        val deleteButton = itemView.findViewById(R.id.delete_button) as ImageView
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.store_name_in_cart_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: DataClassStoreNameInCart = userList[position]
        holder.storePhoto.id = user.idOfPhoto;
        holder.nameOfStoreInCart.text = user.nameOfStore
        holder.itemView.setOnClickListener(){
            v ->
            Log.d("rv","IN")
        }
        holder.deleteButton.setOnClickListener(){
            v ->
            var pos : Int = userList.indexOf(user)
            Log.d("rv",position.toString())
            userList.removeAt(pos)
            notifyItemRemoved(pos)
        }
    }

}