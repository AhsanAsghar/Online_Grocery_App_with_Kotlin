package com.example.medicineandgroceryapp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.HashMap

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
        (holder as ItemsInCategoryViewHolderData).addToCart.setOnClickListener(){
            v ->
            getAlertBar(user.context,user.idOfStore.toInt(),user.customerNumber,user.pid)
        }
    }
    }
    private fun getAlertBar(context: Context, store_id: Int,
                            phone_customer: String,productId: Int) {
        val builder:AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view : View = inflater.inflate(R.layout.dialog_add_into_cart,null)
        val text: EditText = view.findViewById(R.id.number_of_items)
        builder.setView(view)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            if(text.text.toString().length > 0 && text.text.toString().toInt() <= 10){
                val queu = Volley.newRequestQueue(context)
                val url : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/addToCart.php"
                val postRequest =object: StringRequest(Request.Method.POST,url, Response.Listener {
                        response ->
                    Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show()
                }, Response.ErrorListener { error ->
                    Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show()
                }){
                    override fun getParams() : Map<String,String>{
                        val params = HashMap<String,String>()
                        params.put("phone", phone_customer.toString())
                        params.put("storeID",store_id.toString())
                        params.put("productID",productId.toString())
                        params.put("quantity",text.text.toString())
                        return params
                    }
                }
                queu.add(postRequest)
                Toast.makeText(context,text.text,Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,"Enter a number, less than 10",Toast.LENGTH_SHORT).show()
            }
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->

        })
        builder.create().show()
    }
}