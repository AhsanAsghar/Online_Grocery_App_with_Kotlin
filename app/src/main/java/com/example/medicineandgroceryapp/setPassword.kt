package com.example.medicineandgroceryapp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class setPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_password)
        var textView  : TextView = findViewById(R.id.password)
        //val intent = getIntent()
        ///var phone : String = intent.getStringExtra("phone")
        textView.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    var password : String = textView.text.toString()
                    val queue = Volley.newRequestQueue(applicationContext)
                    var url : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/setOrComparePassword.php"
                    val postRequest = object : StringRequest(Request.Method.POST,url,Response.Listener {
                        response ->
                        Toast.makeText(applicationContext,response.toString(),Toast.LENGTH_SHORT).show()
                    },Response.ErrorListener {
                        error ->
                        Toast.makeText(application,error.toString(),Toast.LENGTH_SHORT).show()
                    }) {
                        override fun getParams(): MutableMap<String, String> {
                            val params = HashMap<String,String>()
                           // params.put("phone",phone)
                            params.put("existance","False")
                            params.put("password",password)
                            return params
                        }
                    }
                    return true
                }
                else{
                    return false
                }

            }
        })

    }

}
