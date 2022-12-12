package com.example.myfitv2.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfitv2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class SetYourPersonalDataActivity : AppCompatActivity() {

    var edName: EditText? = null
    var edSurname: EditText? = null
    var edGender: EditText? = null
    var edAge: EditText? = null
    var btSubmit: Button? = null

    var mAuth: FirebaseAuth? = null
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_your_personal_data)

        edName = findViewById(R.id.edName)
        edSurname = findViewById(R.id.edSurname)
        edGender = findViewById(R.id.edGender)
        edAge = findViewById(R.id.edAge)
        btSubmit = findViewById(R.id.btSubmit)
        mAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance("https://myfitv3-760b1-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")

        btSubmit?.setOnClickListener {insertDataForNewUser()}
    }

    private fun insertDataForNewUser() {

        val userName    = edName?.text.toString()
        val userSurname = edSurname?.text.toString()
        val userGender  = edGender?.text.toString()

        try {val userAgeCheck = edAge?.text.toString().toInt()}
        catch (e : Exception) {edAge?.error = "Incorrect age format"}

        val userAge = edAge?.text.toString().toInt()

        val isValid : Boolean = userDataValidation(userName,userSurname,userAge.toString(),userGender)

        if (isValid) {
            val user : String = mAuth?.currentUser?.email.toString().replace(".","")
            val duRef : DatabaseReference = dbRef.child(user)
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val map: MutableMap<String, Any> = HashMap()
                    map["userAge"] = userAge
                    map["userGender"] = userGender
                    map["userName"] = userName
                    map["userSurname"] = userSurname
                    map["new"] = false
                    duRef.ref.updateChildren(map)

                    showNotification("Your profile has been updated !")
                    startActivity(Intent(this@SetYourPersonalDataActivity, MainMenuActivity::class.java))

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun userDataValidation (name : String,surname : String, age : String, gender : String) : Boolean {
        if (name.isEmpty()) {
            edName?.error = "Please enter name"
            return false
        }
        if (surname.isEmpty()) {
            edSurname?.error = "Please enter surname"
            return false
        }
        if (age.isEmpty()) {
            edAge?.error = "Please enter age"
            return false
        }
        if (gender.isEmpty()) {
            edGender?.error = "Please enter gender"
            return false
        } else {
            try {
                val ageValidation = age.toInt()
                if (ageValidation <= 0 ) {
                    edAge?.error = "Age cannot be negative !"
                    return false
                }
            } catch (e : Exception) {
                edAge?.error = "Age must be a positive integer !"
                return false
            }

            val pattern = Regex("^[A-Za-z]+\$")
            return if (pattern.containsMatchIn(name)) {
                if (pattern.containsMatchIn(surname)) {
                    if (pattern.containsMatchIn(gender)) {
                        true
                    } else { edGender?.error = "Incorrect gender format."
                        false
                    }
                } else { edSurname?.error = "Incorrect surname format."
                    false
                }
            } else { edName?.error = "Incorrect name format."
                false
            }
        }
    }

    private fun showNotification (notification : String) {Toast.makeText(this@SetYourPersonalDataActivity,notification,Toast.LENGTH_SHORT).show() }



}