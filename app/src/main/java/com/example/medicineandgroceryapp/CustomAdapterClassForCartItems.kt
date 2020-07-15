package com.example.medicineandgroceryapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class CustomAdapterClassForCartItems(val userList: ArrayList<DataClassForCartItems>) : RecyclerView.Adapter<CustomAdapterClassForCartItems.ViewHolder>() {
    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val nameOfitem = itemView.findViewById(R.id.cart_item_name) as TextView
        val itemPhoto = itemView.findViewById(R.id.cart_item_photo) as ImageView
        val priceOfItem = itemView.findViewById(R.id.price_status) as TextView
        val deleteButton = itemView.findViewById(R.id.delete_button_cart_items) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.cart_items_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: DataClassForCartItems = userList[position]
        holder.itemPhoto.setImageBitmap(user.photoBitmap);
        holder.nameOfitem.text = user.nameOfItem
        holder.priceOfItem.text = user.priceOfItem
        holder.itemView.setOnClickListener(){
            v ->

        }
        holder.deleteButton.setOnClickListener(){
            v ->
            val queue = Volley.newRequestQueue(user.context)
            val url : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/deleteProductFromCart.php"
            val postRequest =object: StringRequest(Request.Method.POST,url, Response.Listener {
                    response ->
                Toast.makeText(user.context,response.toString(), Toast.LENGTH_SHORT).show()
                val pos : Int = userList.indexOf(user)
                userList.removeAt(pos)
                notifyItemRemoved(pos)
            }, Response.ErrorListener { error ->
                Log.d("Error",error.toString())
                Toast.makeText(user.context,error.toString(), Toast.LENGTH_LONG).show()
            }){
                override fun getParams() : Map<String,String>{
                    val params = HashMap<String,String>()
                    params.put("cartId",user.cartId)
                    return params
                }
            }
            queue.add(postRequest)
        }
    }

}