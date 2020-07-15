package com.example.medicineandgroceryapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
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
        holder.storePhoto.setImageBitmap(user.idOfPhoto);
        holder.nameOfStoreInCart.text = user.nameOfStore
        holder.itemView.setOnClickListener(){
            v ->
            val intent = Intent(user.context,cart_items::class.java)
            intent.putExtra("phone",user.phone)
            intent.putExtra("id",user.id_of_store)
            intent.putExtra("name",user.nameOfStore)
            val image = Bitmap.createScaledBitmap(user.idOfPhoto, 70, 70, true);
            intent.putExtra("image", image)
            user.context.startActivity(intent)
        }
        holder.deleteButton.setOnClickListener(){
            v ->
            val queue = Volley.newRequestQueue(user.context)
                val queu = Volley.newRequestQueue(user.context)
                var url : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/deleteStoreFromCart.php"
                val postRequest =object: StringRequest(Request.Method.POST,url, Response.Listener {
                        response ->
                    Toast.makeText(user.context,response.toString(), Toast.LENGTH_SHORT).show()
                    var pos : Int = userList.indexOf(user)
                    userList.removeAt(pos)
                    notifyItemRemoved(pos)
                }, Response.ErrorListener { error ->
                    Log.d("Error",error.toString())
                    Toast.makeText(user.context,error.toString(), Toast.LENGTH_LONG).show()
                }){
                    override fun getParams() : Map<String,String>{
                        val params = HashMap<String,String>()
                        params.put("store_id",user.id_of_store)
                        return params
                    }
                }
                queue.add(postRequest)

        }
    }
    fun stringToBitmap(imageInString : String) : Bitmap {
        val imageBytes = Base64.decode(imageInString,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return image
    }
}