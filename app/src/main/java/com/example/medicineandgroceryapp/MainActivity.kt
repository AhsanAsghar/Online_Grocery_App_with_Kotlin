package com.example.medicineandgroceryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            val intent =
                Intent(this@MainActivity,UserNavigation::class.java)
            intent.putExtra("phone",user.phoneNumber.toString())
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            this@MainActivity.finish()
        }else{
            val hd: Handler = Handler()
            hd.postDelayed(
                {
                    val intent =
                        Intent(this@MainActivity,login_with_phone::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    this@MainActivity.finish()
                }, 1000
            )
        }

    }


}
