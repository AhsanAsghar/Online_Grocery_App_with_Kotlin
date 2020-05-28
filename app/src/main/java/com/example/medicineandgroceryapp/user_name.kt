package com.example.medicineandgroceryapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*


class user_name : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_name)
        var phone: String? = "+923450694449"
        /*if (intent != null) {
            Log.d("PhoneAuth","IN")
            var intent = intent
            phone = intent.getStringExtra("phone")
        }else{
            phone = "+923450694449"
        }*/
        Log.d("PhoneAuth",phone.toString())
        var nameEditText : EditText = findViewById(R.id.personaldata_name)
        var passEditText : EditText = findViewById(R.id.password)
        var repassEditText : EditText = findViewById(R.id.re_enter_password)
        var nextButton : Button = findViewById(R.id.button_next)
        var pass = passEditText.text.toString()
        nextButton.setOnClickListener(){
            v ->
            if(nameEditText.text.toString().equals(null)|| passEditText.text.equals(null) || repassEditText.text.equals(null)){
                Toast.makeText(applicationContext,"Fill all Fields",Toast.LENGTH_SHORT).show()
            }
            else if(passEditText.text.toString().equals(repassEditText.text.toString())&&!checkPass((pass))){
                val queu = Volley.newRequestQueue(applicationContext)
                var url : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/updatePasswordName.php"
                val postRequest =object: StringRequest(Request.Method.POST,url, Response.Listener {
                        response ->
                    Toast.makeText(applicationContext,response.toString(),Toast.LENGTH_SHORT).show()
                },Response.ErrorListener {error ->
                    Toast.makeText(applicationContext,error.toString(),Toast.LENGTH_SHORT).show()
                }){
                    override fun getParams() : Map<String,String>{
                        val params = HashMap<String,String>()
                        Log.d("PhoneAuth",phone)
                        params.put("phone", phone.toString())
                        params.put("name",nameEditText.text.toString())
                        params.put("password",passEditText.text.toString())
                        return params
                    }
                }
                queu.add(postRequest)
            }else{
                Toast.makeText(applicationContext,"Password mismatch",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun checkPass(pass: String): Boolean {
        if(pass.length == 8){
            return true
        }
        else{
            Toast.makeText(applicationContext,"Enter 8 characters", Toast.LENGTH_SHORT).show()
            return false
        }

    }
}

