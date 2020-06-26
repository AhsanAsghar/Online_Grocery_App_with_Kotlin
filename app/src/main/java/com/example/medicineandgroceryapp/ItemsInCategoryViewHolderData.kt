package com.example.medicineandgroceryapp

import android.graphics.Bitmap
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ItemsInCategoryViewHolderData(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var productName: TextView
    var productPrice: TextView
    var productImage: ImageView
    var addToCart: ImageButton
    fun setProductName(pn: String?) {
        productName.setText(pn)
    }

    fun setProductPrice(pp: String?) {
        productPrice.setText(pp)
    }

    fun setProductImage(pa: Bitmap) {
        productImage.setImageBitmap(pa)
    }

    init {
        productName = itemView.findViewById<View>(R.id.item_name) as TextView
        productPrice = itemView.findViewById<View>(R.id.item_price) as TextView
        productImage =
            itemView.findViewById<View>(R.id.item_image) as ImageView
        addToCart = itemView.findViewById<View>(R.id.add_to_cart) as ImageButton
    }
}
