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
import kotlinx.android.synthetic.main.activity_personal_data.*
import kotlinx.android.synthetic.main.map_dialogue.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class personal_data : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data)
        var phone:String = intent.getStringExtra("phone")
        val nameEditText : EditText = findViewById(R.id.personaldata_name)
        val passEditText : EditText = findViewById(R.id.password)
        val repassEditText : EditText = findViewById(R.id.re_enter_password)
        val nextButton : Button = findViewById(R.id.button_next)
        nextButton.setOnClickListener(){
            v ->
            Log.d("test",phone)
            Log.d("test",passEditText.text.toString())
            Log.d("test",repassEditText.text.toString())
            if(nameEditText.text.toString().equals(null)|| passEditText.text.equals(null) || repassEditText.text.equals(null)){
                Toast.makeText(applicationContext,"Fill all Fields",Toast.LENGTH_SHORT).show()
            }
            else if(passEditText.text.toString() == repassEditText.text.toString() && checkPass((passEditText.text.toString()))){
                val queu = Volley.newRequestQueue(applicationContext)
                val url : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/updatePasswordName.php"
                val postRequest =object: StringRequest(Request.Method.POST,url, Response.Listener {
                        response ->
                    val intent = Intent(this@personal_data,UserNavigation::class.java)
                    intent.putExtra("phone",phone)
                    startActivity(intent)

                },Response.ErrorListener {error ->
                    Toast.makeText(applicationContext,error.toString(),Toast.LENGTH_SHORT).show()
                }){
                    override fun getParams() : Map<String,String>{
                        val params = HashMap<String,String>()
                        params.put("phone", phone)
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

