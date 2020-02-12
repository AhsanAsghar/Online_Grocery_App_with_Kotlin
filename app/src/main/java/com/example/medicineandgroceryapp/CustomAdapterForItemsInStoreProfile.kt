package com.example.medicineandgroceryapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CustomAdapterForItemsInStoreProfile (val userList: ArrayList<DataItemsInStoreProfileAbstract>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object{
        const val LABEL = 0
        const val DATA = 1
    }
    override fun getItemViewType(position: Int): Int {
        val type = when(userList[position].type) {
            DataItemsInCategoryParent.TYPE.LABEL -> LABEL
            DataItemsInCategoryParent.TYPE.DATA -> DATA
            else-> LABEL
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == LABEL){
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_in_store_profile_label,parent,false)
            val viewHolderLabel = ItemsInStoreProfileVIewHolderLabel(v)
            return viewHolderLabel
        }
        else if(viewType == DATA){
            //Log.d("Error","error")
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_in_store_profile,parent,false)
            val viewHolderData = ItemsInStoreProfileViewHolderData(v)
            return viewHolderData
        }
        else{
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_in_store_profile_label,parent,false)
            val viewHolderLabel = ItemsInStoreProfileViewHolderData(v)
            return viewHolderLabel
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.itemViewType == LABEL) {
            val user = userList[position] as DataClassForItemsInStoreProfileLabel
            (holder as ItemsInStoreProfileVIewHolderLabel).setLabel(user.label1)
        }else if(holder.itemViewType == DATA){

            val user = userList[position] as DataClassForItemsInStoreProfile
            (holder as ItemsInStoreProfileViewHolderData).setProductImage(user.idOfImageStoreProfileItems)
            (holder as ItemsInStoreProfileViewHolderData).setProductName(user.nameOfStoreProfileItem)
            (holder as ItemsInStoreProfileViewHolderData).setProductPrice(user.priceOfStoreProfileItems)
        }
    }

}