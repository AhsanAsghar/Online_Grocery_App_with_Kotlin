package com.example.medicineandgroceryapp

import android.content.Intent
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterForNearbyStores (val userList: ArrayList<DataClassForNearbyStores>) : RecyclerView.Adapter<CustomAdapterForNearbyStores.ViewHolder>(){
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val nameOfStore = itemView.findViewById(R.id.storeName) as TextView
        val distance = itemView.findViewById(R.id.distance) as TextView
        val photo = itemView.findViewById(R.id.imageOfStore) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.store_list_nearby, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user:DataClassForNearbyStores = userList[position]
        holder.nameOfStore.text = user.nameOfStore
        holder.distance.text = user.distance
        holder.photo.setImageBitmap(user.idOfPhoto)
        val storePhone = user.storePhoneStore
        val context = user.context
        holder.itemView.setOnClickListener(){
            Toast.makeText(user.context,"Onclick",Toast.LENGTH_SHORT).show()
            val intent = Intent(context, ItemsInCategory::class.java)
            intent.putExtra("phone",storePhone)
            intent.putExtra("idOfStore",user.idOfStore)
            context.startActivity(intent)
        }
    }
}