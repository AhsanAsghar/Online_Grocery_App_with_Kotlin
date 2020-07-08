package com.example.medicineandgroceryapp
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class login_with_phone : AppCompatActivity() {
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var verficationInProgress : Boolean = false
    var codeNotVerified : Boolean = true
    var phoneNumberExistInDB : Boolean = true
    private var phone : String = ""
    private lateinit var auth: FirebaseAuth
    companion object{
        private const val KEY_VERIFY_IN_PROGRESS = "Verification Process"
        private const val TAG = "PhoneAuth"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_with_phone)
        auth = FirebaseAuth.getInstance()
        var textView: TextView = findViewById(R.id.phone_number_customer)
        val mVerificationField: EditText = findViewById(R.id.verfication_field)
        val mVerifyButton : Button = findViewById(R.id.verify)
        mVerificationField.visibility = View.GONE
        mVerifyButton.visibility = View.GONE
        mVerificationField.isEnabled = false
        mVerifyButton.isEnabled = false
        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Toast.makeText(applicationContext,"Verrification Succesful",Toast.LENGTH_SHORT).show()
                codeNotVerified = false
                signInWIthPhoneNumber(p0)
                //To change body of created functions use File | Settings | File Templates.
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(applicationContext,"Verfication Failed",Toast.LENGTH_SHORT).show() //To change body of created functions use File | Settings | File Templates.
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                if(codeNotVerified){
                    Toast.makeText(applicationContext,"Different Device",Toast.LENGTH_SHORT).show()
                    textView.isEnabled = false
                    textView.visibility = View.GONE
                    mVerificationField.visibility = View.VISIBLE
                    mVerifyButton.visibility = View.VISIBLE
                    mVerificationField.isEnabled = true
                    mVerifyButton.isEnabled = true
                    mVerifyButton.setOnClickListener(){
                        v ->
                        val credential = PhoneAuthProvider.getCredential(p0!!,mVerificationField.text.toString())
                        signInWIthPhoneNumber(credential)
                    }
                }
            }

        }
        textView.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    phone = textView.text.toString()
                    val phone1 = makePhoneNumber(phone)
                    if(phone1 != null){
                        phone = phone1
                        startPhoneVerificationBySendingCode(phone1)
                    }
                    /*if (phone1 != null) {
                        phone = phone1
                        val queu = Volley.newRequestQueue(applicationContext)
                        var url : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/checkIfNumberExists.php"
                        val postRequest =object: StringRequest(Request.Method.POST,url,Response.Listener {
                                response ->
                            if(response.equals("[\"Record Exists\"]")){
                                Toast.makeText(applicationContext,response.toString(),Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext,checkPassword::class.java)
                                intent.putExtra("phone", phone1)
                                startActivity(intent)
                            }else{
                                Log.d(TAG,"here")
                                phoneNumberExistInDB = false
                                if(phone1 != null){
                                    startPhoneVerificationBySendingCode(phone1)
                                }
                                Toast.makeText(applicationContext,response.toString(),Toast.LENGTH_SHORT).show()
                            }


                        },Response.ErrorListener {error ->
                            Toast.makeText(applicationContext,error.toString(),Toast.LENGTH_SHORT).show()
                        }){
                            override fun getParams() : Map<String,String>{
                                val params = HashMap<String,String>()
                                if(phone1 != null)
                                    params.put("phone",phone1)
                                return params
                            }
                        }
                        queu.add(postRequest)
                        return true
                    }
                    else{
                        Toast.makeText(applicationContext,"Phone number is wrong",Toast.LENGTH_SHORT).show()
                        return false
                    }*/
                    }
                return false
            }
        })

    }
    private fun makePhoneNumber(phone_f : String) : String?{
        if((phone_f.contains("+92") && phone.length == 13)){
            Log.d(TAG,phone_f)
            return phone_f
        }else if(phone_f.contains("0") && phone.length == 11){
            var removePrefix = phone_f.removePrefix("0")
            removePrefix = "+92" + removePrefix
            Log.d(TAG,removePrefix)
            return removePrefix
        }
        else{
            return null
        }
    }

    private fun startPhoneVerificationBySendingCode(phoneNumber : String){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            mCallBack
        )
        verficationInProgress = true
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        if (outState != null) {
            outState.putBoolean(KEY_VERIFY_IN_PROGRESS,verficationInProgress)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            verficationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS)
        }
    }

    private fun signInWIthPhoneNumber(credential: PhoneAuthCredential){
        auth.signInWithCredential(credential)
            .addOnCompleteListener(){task ->
                if(task.isSuccessful){
                    Log.d(TAG,"SignInSuccesful")
                    val queu = Volley.newRequestQueue(applicationContext)
                    var url : String = "https://grocerymedicineapp.000webhostapp.com/PHPfiles/signUpWIthPhoneNumber.php"
                    val postRequest =object: StringRequest(Request.Method.POST,url,Response.Listener {
                            response ->
                        Log.d(TAG,"Data entered")
                        Log.d(TAG,phone)
                        Toast.makeText(applicationContext,response.toString(),Toast.LENGTH_SHORT).show()
                        val resultfull  = response.toString().split(":").toTypedArray()
                        val result = resultfull[1].substring(1,resultfull[1].length - 2)
                        if(result.equals("NO")){
                            val intent = Intent(applicationContext,UserNavigation::class.java)
                            intent.putExtra("phone", phone)
                            startActivity(intent)
                        }else{
                            val intent = Intent(applicationContext,personal_data::class.java)
                            intent.putExtra("phone", phone)
                            startActivity(intent)
                        }

                    },Response.ErrorListener {error ->
                        Log.d(TAG,error.toString())
                        Toast.makeText(applicationContext,error.toString(),Toast.LENGTH_SHORT).show()
                    }){
                        override fun getParams() : Map<String,String>{
                            val params = HashMap<String,String>()
                            Log.d(TAG,phone)
                            params.put("phone", phone)
                            return params
                        }
                    }
                    queu.add(postRequest)
                }
                else{
                    Log.d(TAG,"Sign in failed")
                }

            }
    }

    override fun onStart() {
        super.onStart()
        var phone1 = makePhoneNumber(phone)
        if(verficationInProgress && phone1 != null){
            startPhoneVerificationBySendingCode(phone1)
        }
    }

}
