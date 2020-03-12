package com.example.medicineandgroceryapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ItemsInStoreProfileViewHolderData(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var productName: TextView
    var productPrice: TextView
    var productImage: ImageView
    var deleteButton : ImageView
    fun setProductName(pn: String?) {
        productName.text = pn
    }

    fun setProductPrice(pp: String?) {
        productPrice.text = pp
    }

    fun setProductImage(pa: Int) {
        productImage.id = pa
    }


    init {
        productName = itemView.findViewById<View>(R.id.item_name_for_store_profile_items) as TextView
        productPrice = itemView.findViewById<View>(R.id.price_for_store_profile_items) as TextView
        productImage =
            itemView.findViewById<View>(R.id.item_photo_in_store_profile_items) as ImageView
        deleteButton = itemView.findViewById(R.id.delete_items_in_store_profile) as ImageView
    }
}
