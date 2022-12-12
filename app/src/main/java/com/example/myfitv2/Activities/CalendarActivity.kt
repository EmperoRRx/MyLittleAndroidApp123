package com.example.myfitv2.Activities


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitv2.Fragments.CalendarFragment
import com.example.myfitv2.DataClasses.CalendarHolder
import com.example.myfitv2.DataClasses.Friend
import com.example.myfitv2.R
import com.example.myfitv2.databinding.ActivityCalendarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class CalendarActivity() : AppCompatActivity() {

    internal lateinit var binding: ActivityCalendarBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference


    private var calendarList  : List<CalendarHolder>? = null
    private var calendarViewAdapter : CalendarViewAdapter? = null

    private var user : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        user = mAuth.currentUser?.email.toString().replace(".", "")
        dbRef = FirebaseDatabase.getInstance("https://myfitv3-760b1-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(user!!).child("friends")
        calendarList = listOf(CalendarHolder("My Calendar", mAuth.currentUser?.email.toString(), vertical) { CalendarFragment() })


        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val friend = snapshot.getValue(Friend::class.java)
                    Log.d("friend",friend?.friendEmail.toString());
                    calendarList = calendarList!!.plus(CalendarHolder(friend?.friendName+ "'s Calendar",friend?.friendEmail.toString(),vertical) { CalendarFragment() })
                }

                calendarViewAdapter = CalendarViewAdapter (calendarList!!) {
                    val fragment = it.createView()
                    val anim = it.animation

                    val args = Bundle()
                    args.putString("email",it.subtitleRes)
                    fragment.arguments = args
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(anim.enter, anim.exit, anim.popEnter, anim.popExit)
                        .add(R.id.homeContainer, fragment, fragment.javaClass.simpleName)
                        .addToBackStack(fragment.javaClass.simpleName)
                        .commit()
                }

                binding = ActivityCalendarBinding.inflate(layoutInflater)
                setContentView(binding.root)
                setSupportActionBar(binding.activityToolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                binding.examplesRecyclerview.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    adapter = calendarViewAdapter
                    addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> onBackPressed().let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

}