package com.example.myfitv2.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myfitv2.Fragments.FriendsFragment
import com.example.myfitv2.R


class ShowFriendsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_list_holder)
        replaceFragment(FriendsFragment())
    }

    private fun replaceFragment(fragment : Fragment) {
        val fragmentManager  = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

}