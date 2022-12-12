package com.example.myfitv2.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.myfitv2.Fragments.WorkoutFragment
import com.example.myfitv2.R

class ShowAllWorkouts : AppCompatActivity()  {

    private var currentEmail : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_list_holder)

        currentEmail = intent.getStringExtra("email")
        replaceFragment(WorkoutFragment())
    }

    private fun replaceFragment(fragment : Fragment) {

        val fragmentManager  = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val args = Bundle()
        args.putString("currentEmail",currentEmail)
        fragment.arguments = args
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()

    }
}

