package com.example.myfitv2.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.myfitv2.Fragments.ExerciseFragment
import com.example.myfitv2.R

class ShowAllExercises : AppCompatActivity()  {

    private var currentWorkout : String? = null
    private var currentEmail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list_holder)

        currentWorkout = intent.getStringExtra("currentWorkout")
        currentEmail = intent.getStringExtra("currentEmail")
        replaceFragment(ExerciseFragment())
    }

    private fun replaceFragment(fragment : Fragment) {

        try {
            val fragmentManager  = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val args = Bundle()
            args.putString("currentWorkout",currentWorkout)
            args.putString("currentEmail",currentEmail)
            fragment.arguments = args

            fragmentTransaction.replace(R.id.frame_layout,fragment)
            fragmentTransaction.commit()

        } catch (e : Exception) {Log.d("Show All Exe error ",e.message.toString());}
    }
}

