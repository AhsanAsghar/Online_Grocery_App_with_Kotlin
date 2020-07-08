package com.example.medicineandgroceryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class biodata_of_deliveryperson : AppCompatActivity() {
    var phone: String = "null"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biodata_of_deliveryperson)
        phone = intent.getStringExtra("phone")
        val bikeNumberET : EditText = findViewById(R.id.bike_number)
        val licenseNumberET : EditText = findViewById(R.id.lisence_number)
        val emailAddressET : EditText = findViewById(R.id.deliverperson_email)
        val next : Button =findViewById(R.id.delivery_bio_next)
        next.setOnClickListener {
                v ->
            if(!checkEmail(emailAddressET.text.toString())){
                emailAddressET.error = "Fill it correctly"
            }
            if(!checkLicenseNumber(licenseNumberET.text.toString())){
                licenseNumberET.error = "fill it Correctly"
            }
            if(!checkBikeNumber(bikeNumberET.text.toString())){
                bikeNumberET.error = "Fill it correctly"
            }
            if(checkEmail(emailAddressET.text.toString())
                && checkBikeNumber(bikeNumberET.text.toString()) && checkLicenseNumber(licenseNumberET.text.toString())){
                val bikeNumber = bikeNumberET.text.toString()
                val licenseNumber = licenseNumberET.text.toString()
                val emailAddress = emailAddressET.text.toString()
                val queu = Volley.newRequestQueue(applicationContext)
                var url : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/biodata_delivery_person.php"
                val postRequest =object: StringRequest(Request.Method.POST,url, Response.Listener {
                        response ->
                    val result  = response.toString().split(":").toTypedArray()
                    val yesORno = result[1].substring(1,result[1].length - 2)
                    if(yesORno.equals("YES")){
                        val intent = Intent(this@biodata_of_deliveryperson,delivery_person_profile::class.java)
                        intent.putExtra("phone",phone)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this,"Problem in query",Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener { error ->
                    Toast.makeText(applicationContext,error.toString(), Toast.LENGTH_SHORT).show()
                }){
                    override fun getParams() : Map<String,String>{
                        val params = HashMap<String,String>()
                        params.put("phone",phone)
                        params.put("bikeNumber",bikeNumber.toString())
                        params.put("license",licenseNumber.toString())
                        params.put("email",emailAddress.toString())
                        return params
                    }
                }
                queu.add(postRequest)
            }else{
                Toast.makeText(applicationContext,"Please fill all fields", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun checkEmail(email : String) : Boolean{
        if(email.contains("@") && !email.equals(null) && email.contains(".com")){
            return true
        }
        return false
    }
    private fun checkBikeNumber(bikeNumber: String) : Boolean{
        val pattern1 = "[a-zA-Z] [1-9]".toRegex()
        val pattern2 = "[a-zA-Z][1-9]".toRegex()
        val match1 = pattern1.find(bikeNumber)
        val match2 = pattern2.find(bikeNumber)
        val id1 = match1?.value
        val id2= match2?.value
        if(bikeNumber.length >= 4 && (id1 != null || id2 != null) && !bikeNumber.equals(null)) {
            return true
        }
        return false
    }
    private fun checkLicenseNumber(licenseNumber : String) : Boolean{
        val pattern = "[1-9]".toRegex()
        val match = pattern.find(licenseNumber)
        val id1 = match?.value
        if(id1 != null && !licenseNumber.equals(null)){
            return true
        }
        return false
    }

}
