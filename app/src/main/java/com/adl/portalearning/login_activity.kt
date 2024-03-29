package com.adl.portalearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class login_activity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        buttonLogin.setOnClickListener({
            login()
        })

        buttonRegister.setOnClickListener({
            val intent = Intent(this@login_activity,register_activity::class.java)
            startActivity(intent)
        })
    }

    private fun login() {
        auth.signInWithEmailAndPassword(inputUsername.text.toString(),inputPasword.text.toString()).addOnCompleteListener{
                it ->
            if (it.isSuccessful){
                if(inputUsername.text.toString() == "admin@gmail.com "){
                    val intent = Intent(this,Add_Content_Activity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }.addOnFailureListener{exception ->
            Toast.makeText(applicationContext,"username, atau password anda salah",
                Toast.LENGTH_LONG).show()
        }
    }

}