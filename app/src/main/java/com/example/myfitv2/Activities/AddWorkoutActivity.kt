package com.example.myfitv2.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.myfitv2.DataClasses.Exercise
import com.example.myfitv2.DataClasses.Workout
import com.example.myfitv2.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AddWorkoutActivity : AppCompatActivity() {


    var workoutNameTxt: TextInputEditText? = null
    var repsTxt: TextInputEditText? = null
    var seriesTxt: TextInputEditText? = null
    var exeNameTxt: TextInputEditText? = null
    var btnWorkoutName: Button? = null
    var btnExit: Button? = null
    var user : String? = null


    var mAuth: FirebaseAuth? = null
    private lateinit var dbRef: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workout)


        workoutNameTxt = findViewById(R.id.workoutName)
        btnWorkoutName = findViewById(R.id.addWorkoutName)
        btnExit= findViewById(R.id.exitBtn)
        repsTxt = findViewById(R.id.repsTxt)
        seriesTxt = findViewById(R.id.seriesTxt)
        exeNameTxt = findViewById(R.id.exeNameTxt)


        mAuth = FirebaseAuth.getInstance()
        user = mAuth?.currentUser?.email.toString().replace(".", "")
        dbRef = FirebaseDatabase.getInstance("https://myfitv3-760b1-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(user!!).child("workouts")



        btnWorkoutName?.setOnClickListener(View.OnClickListener { addWorkout() })
        btnExit?.setOnClickListener(View.OnClickListener  {startActivity( Intent(this,WorkoutMenuActivity::class.java))})

    }


    private fun addWorkout() {

        val isValid : Boolean = workoutValidation()
        if (isValid)
        {
            val workoutName = workoutNameTxt!!.text.toString().lowercase()
            val exeName = exeNameTxt!!.text.toString().lowercase()
            val series = seriesTxt!!.text.toString().toInt()
            val reps = repsTxt!!.text.toString().toInt()


            dbRef.addListenerForSingleValueEvent (object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.hasChild(workoutName)) {

                        if (dataSnapshot.child(workoutName).hasChild(exeName)) {showNotification("This exercise is present in this workout")}
                        else {
                            val exe = Exercise(exeName, reps, series,0.0,0.0)

                            dbRef.child(workoutName).child("exercises").child(exeName).setValue(exe).addOnCompleteListener {
                                showNotification("Added ")
                            }.addOnFailureListener { err -> showNotification("Error ${err.message}") }

                            exeNameTxt!!.text?.clear()
                            seriesTxt!!.text?.clear()
                            repsTxt!!.text?.clear()
                        }

                    } else {
                        val exe = Exercise(exeName, reps, series,0.0,0.0)
                        val workout = Workout(workoutName)

                        dbRef.child(workoutName).setValue(workout).addOnCompleteListener {}.addOnFailureListener { err -> showNotification("Error ${err.message}") }
                        dbRef.child(workoutName).child("exercises").child(exeName).setValue(exe).addOnCompleteListener {showNotification("Added ")}.addOnFailureListener { err -> showNotification("Error ${err.message}") }

                        exeNameTxt!!.text?.clear()
                        seriesTxt!!.text?.clear()
                        repsTxt!!.text?.clear()
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    }


    private fun showNotification (notification : String) { Toast.makeText(this@AddWorkoutActivity,notification, Toast.LENGTH_SHORT).show() }



    private fun workoutValidation (): Boolean {

        val workoutName = workoutNameTxt!!.text.toString()
        val exeName = exeNameTxt!!.text.toString()
        val series = seriesTxt!!.text.toString()
        val reps = repsTxt!!.text.toString()

        return if (TextUtils.isEmpty(workoutName)) {
            workoutNameTxt!!.error = "Workout name  cannot be empty"
            workoutNameTxt!!.requestFocus()
            false
        } else if (TextUtils.isEmpty(exeName)) {
            exeNameTxt!!.error = "Exercise name  cannot be empty"
            exeNameTxt!!.requestFocus()
            false
        } else if (TextUtils.isEmpty(series)) {
            seriesTxt!!.error = "Series count  cannot be empty"
            seriesTxt!!.requestFocus()
            false
        } else if (TextUtils.isEmpty(reps)) {
            repsTxt!!.error = "Reps count  cannot be empty"
            repsTxt!!.requestFocus()
            false
        } else {
            try {val seriesCheck = series.toInt()}
            catch (e : Exception) {
                seriesTxt!!.error = "Incorrect series format"
                seriesTxt!!.requestFocus()
                return false
            }
            try {val repsCheck = reps.toInt()}
            catch (e : Exception) {
                repsTxt!!.error = "Incorrect reps format"
                repsTxt!!.requestFocus()
                return false
            }
            return true
        }
    }
}

