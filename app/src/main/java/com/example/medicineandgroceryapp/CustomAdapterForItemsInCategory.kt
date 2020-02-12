package com.example.medicineandgroceryapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class CustomAdapterForItemsInCategory (val userList: ArrayList<DataItemsInCategoryParent>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object{
        const val LABEL = 0
        const val DATA = 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == LABEL){
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.items_in_category_label,parent,false)
            val viewHolder1: RecyclerView.ViewHolder = ItemsInCategoryViewHolderLabel(v)
            return viewHolder1
        } else if(viewType == DATA) {
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.items_in_category,parent,false)
            val viewHolder2: RecyclerView.ViewHolder = ItemsInCategoryViewHolderData(v)
            return viewHolder2
        } else{
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.items_in_category,parent,false)
            val viewHolder2: RecyclerView.ViewHolder = ItemsInCategoryViewHolderData(v)
            return viewHolder2
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    override fun getItemViewType(position: Int): Int {
        val type = when(userList[position].type){
            DataItemsInCategoryParent.TYPE.LABEL -> LABEL
            DataItemsInCategoryParent.TYPE.DATA -> DATA
            else -> DATA
        }
        return  type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if(holder.itemViewType == LABEL) {
        val user = userList[position] as DataClassForDataItemsInCategoryLabel
        (holder as ItemsInCategoryViewHolderLabel).setLabel(user.label1)
    }else if(holder.itemViewType == DATA){
        val user = userList[position] as DataClassForDataItemsInCategory
        (holder as ItemsInCategoryViewHolderData).setProductImage(user.idOfImage)
        (holder as ItemsInCategoryViewHolderData).setProductName(user.nameOfItem)
        (holder as ItemsInCategoryViewHolderData).setProductPrice(user.priceOfItem)
    }
    }

}