package com.adl.portalearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class register_activity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth= FirebaseAuth.getInstance()
        btnSaveRegister.setOnClickListener({
            register()
        })
    }

    private fun register() {
        auth.createUserWithEmailAndPassword(inputRegisterUsername.text.toString(), inputRegisterPassword.text.toString()).addOnCompleteListener{
            if (it.isSuccessful){
                finish()
            }
        }.addOnFailureListener{exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    }
