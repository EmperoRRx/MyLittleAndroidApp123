package com.example.myfitv2.Activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfitv2.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class LoginActivity : AppCompatActivity() {


    var etLoginEmail: TextInputEditText? = null
    var etLoginPassword: TextInputEditText? = null
    var tvRegisterHere: TextView? = null
    var btnLogin: Button? = null


    var mAuth: FirebaseAuth? = null
    private lateinit var dbRef: DatabaseReference

    override fun onStart() {
        super.onStart()
        if (mAuth?.currentUser != null) {mAuth?.signOut()}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        etLoginEmail = findViewById(R.id.etLoginEmail)
        etLoginPassword = findViewById(R.id.etLoginPass)
        tvRegisterHere = findViewById(R.id.tvRegisterHere)
        btnLogin = findViewById(R.id.btnLogin)
        mAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance("https://myfitv3-760b1-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")


        btnLogin?.setOnClickListener(View.OnClickListener {loginUser() })
        tvRegisterHere?.setOnClickListener(View.OnClickListener {startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))})

    }


    private fun loginUser() {

        val email = etLoginEmail!!.text.toString()
        val password = etLoginPassword!!.text.toString()

        val isValid : Boolean = loginValidation(email,password)

        if(isValid){
            val userEmail = email.replace(".","")
            dbRef.addListenerForSingleValueEvent (object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.hasChild(userEmail)) {
                        val isLoggedIn : Boolean = dataSnapshot.child(userEmail).child("loggedIn").value.toString().toBoolean()

                        if (!isLoggedIn) {
                            mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    dataSnapshot.child(userEmail).child("loggedIn").ref.setValue(false)
                                    showNotification("Successfully login")
                                    val isNew : Boolean = dataSnapshot.child(userEmail).child("new").value.toString().toBoolean()

                                    if(isNew) { startActivity(Intent(this@LoginActivity, SetYourPersonalDataActivity::class.java)) }
                                    else      { startActivity(Intent(this@LoginActivity, MainMenuActivity::class.java)) }

                                } else { showNotification("Log in Error: " + task.exception?.message.toString())}

                            }
                        } else {etLoginEmail!!.error = "This account is already logged in";etLoginEmail!!.requestFocus()}

                    } else {etLoginEmail!!.error = "Incorrect email address, please check it";etLoginEmail!!.requestFocus()}
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }




    private fun loginValidation (email: String,password: String): Boolean {

        return if (TextUtils.isEmpty(email)) {
            etLoginEmail!!.error = "Email cannot be empty"
            etLoginEmail!!.requestFocus()
            false
        } else if (TextUtils.isEmpty(password)) {
            etLoginPassword!!.error = "Password cannot be empty"
            etLoginPassword!!.requestFocus()
            false
        } else {true}
    }


    private fun showNotification (notification : String) {Toast.makeText(this@LoginActivity,notification,Toast.LENGTH_SHORT).show()}

}

