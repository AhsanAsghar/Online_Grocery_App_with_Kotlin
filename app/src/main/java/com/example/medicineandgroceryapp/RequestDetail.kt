package com.example.medicineandgroceryapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONArray
import org.json.JSONObject

class RequestDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_detail)
        val mToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_request_details);
        setSupportActionBar(mToolbar)
        val recycleOfCategory = findViewById<RecyclerView>(R.id.recyclerView_request_detail)
        recycleOfCategory.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val users = ArrayList<DataClassForRequestDetails>()
        val customerPhone = intent.getStringExtra("phone")
        val store_id = intent.getStringExtra("store_id")
        var nameOfCustomer : String? = null
        var photoOfCustomer: Bitmap? = null
        findViewById<TextView>(R.id.customer_name_request_detail).setText(nameOfCustomer)
        findViewById<ImageView>(R.id.request_detail_photo_customer).setImageBitmap(photoOfCustomer)
        val acceptButton : Button = findViewById(R.id.accept_request_detail)
        acceptButton.isEnabled = false
        var phoneOfStore: String? = null
        val findDeliveryPerson: Button = findViewById(R.id.find_delivery_person_request_details)
        val declineRequest = findViewById<Button>(R.id.decline_request_details)
        val queue = Volley.newRequestQueue(this)
        //Get customer Image and name
        val url_get_image_name : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/getImageAndNameOfCustomer.php?phone=$customerPhone"
        val request_image_name : StringRequest = StringRequest(url_get_image_name, Response.Listener {
                response ->
            val result  = response.toString().split(":").toTypedArray()
            val yesORno = result[1].substring(1,result[1].length - 2)
            if(yesORno.equals("NO")){
                Toast.makeText(this@RequestDetail,yesORno,Toast.LENGTH_SHORT).show()
            }else{
                Log.d("json",response.toString())
                val jObject : JSONObject = JSONObject(response.toString())
                val jsonArray : JSONArray = jObject?.getJSONArray("response")!!
                Log.d("json",jsonArray.toString())
                nameOfCustomer = jsonArray.getJSONObject(0).getString("name")
                findViewById<TextView>(R.id.customer_name_request_detail).setText(nameOfCustomer)
                val image = jsonArray.getJSONObject(0).getString("profile_pic")
                if(image != "null"){
                    photoOfCustomer = stringToBitmap(image)
                    findViewById<ImageView>(R.id.request_detail_photo_customer).setImageBitmap(photoOfCustomer)
                }

            }

        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@RequestDetail,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request_image_name))
        //End getting customer Image and name
        //get status
        val progress = ProgressBar(this@RequestDetail)
        progress.startLoading(true,"Getting data from server")
        val url_get_status : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/getStatusOfRequest.php?storeid=$store_id&phone=$customerPhone"
        val request_status : StringRequest = StringRequest(url_get_status, Response.Listener {
                response ->
            Log.d("json",response.toString())
            val result  = response.toString().split(":").toTypedArray()
            val yesORno = result[1].substring(1,result[1].length - 2)
            Log.d("piq1",yesORno)
            if(yesORno.equals("NO") || yesORno.equals("NULL")){
                Log.d("problem",yesORno)
            }else if(yesORno.equals("send")){
                findViewById<TextView>(R.id.status_request_detail).setText("Requested - Hire Delivery Person")
            }
            else if(yesORno.equals("dsend")){
                findViewById<TextView>(R.id.status_request_detail).setText("Waiting - Delivery Person Response")
            }else if(yesORno.equals("daccept")){
                acceptButton.isEnabled = true
                findViewById<TextView>(R.id.status_request_detail).setText("Delivery Person Accepted Request")
            }
            else if(yesORno.equals("accept")){
                findViewById<TextView>(R.id.status_request_detail).setText("You Have accepted the offer")
            }
            else if(yesORno.equals("reject")) {
                findViewById<TextView>(R.id.status_request_detail).setText("Offer declined")
            } else{
                Toast.makeText(this@RequestDetail,yesORno, Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@RequestDetail,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request_status))
        // end getting status

        //Get products
        val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/cartItemsGet.php?storeid=$store_id&phone=$customerPhone"
        val request : StringRequest = StringRequest(url_get, Response.Listener {
                response ->
            Log.d("json",response.toString())
            val jObject : JSONObject = JSONObject(response.toString())
            val jsonArray : JSONArray = jObject?.getJSONArray("response")!!
            Log.d("json",jsonArray.toString())
            val a = jsonArray.length()
            Log.d("json",a.toString())
            for(y in 0..a-1){
                Log.d("list", "in")
                val product_id = jsonArray.getJSONObject(y).getString("product_id")
                val product_name = jsonArray.getJSONObject(y).getString("product_name")
                val pimageString = jsonArray.getJSONObject(y).getString("product_image")
                val product_img = stringToBitmap(pimageString)
                val productPrice = jsonArray.getJSONObject(y).getString("product_price")
                users.add(DataClassForRequestDetails(product_img,product_name, productPrice))
            }
            val adapter = CustomAdapterClassForRequestDetail(users)
            recycleOfCategory.adapter = adapter
            progress.dismissDialog()
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@RequestDetail,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request))
        //end getting products
        //get hiring status of store owner
        /*val url_get_hiring_status : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkHiringStatusOfStoreOwner.php?storeid=$store_id&phone=$customerPhone"
        val request_hiring_status : StringRequest = StringRequest(url_get_hiring_status, Response.Listener {
                response ->
            var statusOfHiring  = response.toString().split(":").toTypedArray()
            val hiring = statusOfHiring[1].substring(1,statusOfHiring[1].length - 2)
            if(hiring.equals("Null")){
                priceOfDelivery.setText("Hire Delivery Person")
            }else{

            }
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@RequestDetail,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request_hiring_status))*/
        //end getting hiring status of store owner

        //Get store number
        val url_store_number : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/getStoreNumberThroughStoreId.php?store_id=$store_id"
        val request_store_number : StringRequest = StringRequest(url_store_number, Response.Listener {
                response ->
            val result  = response.toString().split(":").toTypedArray()
            val yesORno = result[1].substring(1,result[1].length - 2)
            if(yesORno.equals("NO")){
                Toast.makeText(this@RequestDetail,"Problem in Query",Toast.LENGTH_SHORT).show()
            } else if (yesORno.equals("NRF")){
                Toast.makeText(this@RequestDetail,"Problem in Query",Toast.LENGTH_SHORT).show()
            } else{
                phoneOfStore = yesORno
            }
        }, Response.ErrorListener {
                error ->
            Log.d("json", error.toString())
            Toast.makeText(this@RequestDetail,error.toString(), Toast.LENGTH_SHORT).show()
        })
        queue.add((request_store_number))
        //End getting store number
        //onClicks
        acceptButton.setOnClickListener {
                v ->
            val url_change_status : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/changeStatus.php?storeid=$store_id&phone=$customerPhone&status=accept"
            val request_change_status : StringRequest = StringRequest(url_change_status, Response.Listener {
                    response ->
                val result  = response.toString().split(":").toTypedArray()
                val yesORno = result[1].substring(1,result[1].length - 2)
                if(yesORno.equals("YES")){
                    //Here send the notification
                    //Both numbers are here
                    //Notification should go like this: phoneOfStore-> customerPhone : these are varriables names
                    //Customer should receive this notification. When customer click on notification cart_items.kt should open.
                    //cart_items.kt require customerPhone and store_id in intent to get open
                    sendVerficatonToCustomer(phoneOfStore.toString(),customerPhone,store_id,1)
                    //End sending Notification
                    Toast.makeText(this@RequestDetail,"Request Accepted",Toast.LENGTH_SHORT).show()
                    this.finish()
                    startActivity(intent)
                }
            }, Response.ErrorListener {
                    error ->
                Log.d("json", error.toString())
                Toast.makeText(this@RequestDetail,error.toString(), Toast.LENGTH_SHORT).show()
            })
            queue.add((request_change_status))
        }
        declineRequest.setOnClickListener { v ->
            val url_reject : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/requestReject.php?storeid=$store_id&phone=$customerPhone"
            val request_rejected : StringRequest = StringRequest(url_reject, Response.Listener {
                    response ->
                val result  = response.toString().split(":").toTypedArray()
                val yesORno = result[1].substring(1,result[1].length - 2)
                if(yesORno.equals("YES")){
                    //Here send the notification
                    //Both numbers are here
                    //Notification should go like this: phoneOfStore-> customerPhone : these are varriables names
                    //Customer should receive this notification. When customer click on notification cart_items.kt should open.
                    //cart_items.kt require customerPhone and store_id in intent to get open
                    sendVerficatonToCustomer(phoneOfStore.toString(),customerPhone,store_id,0)
                    //End sending Notification
                    Toast.makeText(this@RequestDetail,customerPhone + " " + store_id,Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RequestDetail,RequestsOfCustomer::class.java)
                    intent.putExtra("phone",customerPhone)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this.finish()
                }
            }, Response.ErrorListener {
                    error ->
                Log.d("json", error.toString())
                Toast.makeText(this@RequestDetail,error.toString(), Toast.LENGTH_SHORT).show()
            })
            queue.add((request_rejected))
        }
        findDeliveryPerson.setOnClickListener { v ->

        }
        //End onClicks
    }

    fun stringToBitmap(imageInString : String) : Bitmap {
        val imageBytes = Base64.decode(imageInString,0)
        val image = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
        return image
    }
    fun sendVerficatonToCustomer(phoneOfStore:String,customerPhone:String,storeId:String,flag:Int){
        val db = FirebaseFirestore.getInstance()
        val data = hashMapOf(
           "owner_id" to phoneOfStore,
            "store_id" to storeId,
            "flag" to flag
        )
        db.collection("OwnerResponse").document(customerPhone)
            .set(data)
            .addOnSuccessListener { documentReference ->
                if(flag==0){
                    Toast.makeText(this,"Product declined", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"Product accepted", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener { e ->
            }
    }
}
