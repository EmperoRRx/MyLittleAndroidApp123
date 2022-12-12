package com.example.myfitv2.Activities


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.example.myfitv2.Fragments.dpToPx2
import com.example.myfitv2.Fragments.inputMethodManager
import com.example.myfitv2.DataClasses.Friend
import com.example.myfitv2.DataClasses.User
import com.example.myfitv2.R
import com.example.myfitv2.databinding.ActivityMainMenuBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainMenuActivity : AppCompatActivity() {
    var btnLogout: Button? = null
    var btnCalendar: Button? = null
    var btnTraining: Button? = null
    var btnFriends: Button? = null
    var user: String = ""
    private var floatingActionButton : FloatingActionButton? = null


    var mAuth: FirebaseAuth? = null
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance("https://myfitv3-760b1-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")

        user = mAuth?.currentUser?.email.toString().replace(".", "")

        floatingActionButton = findViewById(R.id.floatingActionButton)

        btnLogout   = findViewById(R.id.btnLogout)
        btnCalendar = findViewById(R.id.btnCalendar)
        btnTraining = findViewById(R.id.btnTraining)
        btnFriends  = findViewById(R.id.btnFriends)

        btnLogout?.setOnClickListener(View.OnClickListener {
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.child(user).child("loggedIn").ref.setValue(false)
                    mAuth?.signOut()
                    showNotification("You have been successfully logout ")
                    startActivity(Intent(this@MainMenuActivity, LoginActivity::class.java))
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        })

        btnTraining?.setOnClickListener {startActivity(Intent(this@MainMenuActivity, WorkoutMenuActivity::class.java))}

        btnCalendar?.setOnClickListener {startActivity(Intent(this@MainMenuActivity, CalendarActivity()::class.java))}

        btnFriends?.setOnClickListener  {startActivity(Intent(this@MainMenuActivity, ShowFriendsActivity::class.java))}

        floatingActionButton?.setOnClickListener {inputDialog.show()}

    }


    private val inputDialog by lazy {
        val editText = AppCompatEditText(this)
        val layout = FrameLayout(this).apply {
            val padding = dpToPx2(20, this)
            setPadding(padding, padding, padding, padding)
            addView(editText, FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ))
        }
        AlertDialog.Builder(this)
            .setTitle("Add Friend")
            .setView(layout)
            .setPositiveButton("Add") { _, _ ->
                addFriend(editText.text.toString())
                editText.setText("")
            }
            .setNegativeButton(R.string.close, null)
            .create()
            .apply {
                setOnShowListener {
                    editText.requestFocus()
                    context.inputMethodManager
                        .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                }
                setOnDismissListener {
                    context.inputMethodManager
                        .toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                }
            }
    }


    private fun addFriend (friendEmail : String) {

        val emailToCheck = friendEmail.replace(".", "")
        if (emailToCheck == user) { showNotification("You can't add yourself!")}
        else {
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.hasChild(emailToCheck)) {
                        if (dataSnapshot.child(user).child("friends").hasChild(emailToCheck)) {showNotification("This friend is already added")}
                        else {
                            val newFriend = dataSnapshot.child(emailToCheck).getValue(User::class.java)
                            val friend = Friend(newFriend?.userEmail,newFriend?.userName.toString())
                            dbRef.child(user).child("friends").child(emailToCheck).setValue(friend)
                            showNotification("Friend successfully added")
                        }

                    } else {showNotification("There is no user with that email")}
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    private fun showNotification (notification : String) = Toast.makeText(this@MainMenuActivity,notification, Toast.LENGTH_SHORT).show()
}

