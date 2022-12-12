package com.example.myfitv2.Activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfitv2.DataClasses.User
import com.example.myfitv2.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {

    //Text and buttons
    var etRegEmail: TextInputEditText? = null
    var etRegPassword: TextInputEditText? = null
    var tvLoginHere: TextView? = null
    var btnRegister: Button? = null


    //Firebase references
    var mAuth: FirebaseAuth? = null
    private lateinit var dbRef: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etRegEmail = findViewById(R.id.etRegEmail)
        etRegPassword = findViewById(R.id.etRegPass)
        tvLoginHere = findViewById(R.id.tvLoginHere)
        btnRegister = findViewById(R.id.btnRegister)
        dbRef = FirebaseDatabase.getInstance("https://myfitv3-760b1-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
        mAuth = FirebaseAuth.getInstance()


        btnRegister?.setOnClickListener(View.OnClickListener {createUser()})
        tvLoginHere?.setOnClickListener(View.OnClickListener {startActivity( Intent(this@RegisterActivity,LoginActivity::class.java))})
    }



    private fun createUser() {
        val email = etRegEmail!!.text.toString()
        val password = etRegPassword!!.text.toString()


        val isValid : Boolean = registerValidation(email,password)
        if (isValid) {
            mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    showNotification("User registered successfully")

                    try {
                        val userId = dbRef.push().key!!
                        val userEmailForDatabase = email.replace(".","")
                        val user = User(userId,"placeholder","placeholder","placeholder",email,0,
                            loggedIn = false,
                            new = true
                        )

                        dbRef.child(userEmailForDatabase).setValue(user)
                            .addOnCompleteListener {
                                showNotification("You have been successfully registered ! ")
                            }.addOnFailureListener { err -> showNotification("Error ${err.message}")}

                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    } catch (exe : Exception) {Log.d("Register Problem ",exe.message.toString());}
                } else { showNotification("Registration Error: " + task.exception?.message.toString()) }
            }
        }
    }



    private fun registerValidation (email: String,password: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            etRegEmail!!.error = "Email cannot be empty !"
            etRegEmail!!.requestFocus()
            false
        } else if (TextUtils.isEmpty(password)) {
            etRegPassword!!.error = "Password cannot be empty !"
            etRegPassword!!.requestFocus()
            false
        } else {true}
    }

    private fun showNotification (notification : String) {Toast.makeText(this@RegisterActivity,notification,Toast.LENGTH_SHORT).show() }
}