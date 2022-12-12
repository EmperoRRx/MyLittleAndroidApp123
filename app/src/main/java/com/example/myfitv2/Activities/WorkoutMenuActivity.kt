package com.example.myfitv2.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myfitv2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class WorkoutMenuActivity : AppCompatActivity() {

    private var addWorkout  : Button? = null
    private var showWorkout  : Button? = null
    private var exit  : Button? = null

    var mAuth: FirebaseAuth? = null
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_menu)

        addWorkout = findViewById(R.id.addWorkout)
        showWorkout = findViewById(R.id.showWorkout)
        exit = findViewById(R.id.exitBtn2)

        dbRef = FirebaseDatabase.getInstance("https://myfitv3-760b1-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
        mAuth = FirebaseAuth.getInstance()


        addWorkout?.setOnClickListener(View.OnClickListener  { startActivity( Intent(this,AddWorkoutActivity::class.java))})
        exit?.setOnClickListener(View.OnClickListener  { startActivity( Intent(this,MainMenuActivity::class.java))})
        showWorkout?.setOnClickListener {
            val intent = Intent(this@WorkoutMenuActivity,ShowAllWorkouts::class.java)
            val bundle = Bundle()
            bundle.putString("email", mAuth?.currentUser?.email.toString())
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }
}