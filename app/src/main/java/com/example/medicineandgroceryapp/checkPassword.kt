package com.example.medicineandgroceryapp
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class checkPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_password)
        var textView  : TextView = findViewById(R.id.password)
        var phone : String = intent.getStringExtra("phone")
        textView.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    var password : String = textView.text.toString()
                    val queue = Volley.newRequestQueue(applicationContext)
                    val url_get : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkPassword.php?phone=$phone&password=$password"
                    val request : StringRequest = StringRequest(url_get, Response.Listener {
                            response ->
                        var checkPassword  = response.toString().split(":").toTypedArray()
                        val result = checkPassword[1].substring(1,checkPassword[1].length - 2)
                        if(result == "YES"){
                            val intent = Intent(this@checkPassword,UserNavigation::class.java)
                            intent.putExtra("phone",phone)
                            startActivity(intent)
                        }
                        if(result == "NO"){
                            Toast.makeText(this@checkPassword,"Password incorrect",Toast.LENGTH_SHORT).show()
                        }

                    }, Response.ErrorListener {
                            error ->
                        Log.d("json", error.toString())
                        Toast.makeText(this@checkPassword,error.toString(), Toast.LENGTH_SHORT).show()
                    })
                    queue.add((request))
                    return true
                }
                else{
                    return false
                }

            }
        })

    }

}
