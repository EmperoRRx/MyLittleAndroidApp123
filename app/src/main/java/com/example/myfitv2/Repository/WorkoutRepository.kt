package com.example.myfitv2.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myfitv2.DataClasses.Workout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class WorkoutRepository  (currentEmail : String) {

    private var _currentEmail: String = currentEmail.replace(".", "")
    var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    var  user = mAuth?.currentUser?.email.toString().replace(".", "")


    @Volatile private  var INSTANCE : WorkoutRepository ?= null

    fun getInstance(currentEmail : String) : WorkoutRepository{
        return INSTANCE ?: synchronized(this){

            val instance = WorkoutRepository(currentEmail)
            INSTANCE = instance
            instance
        }
    }


    fun loadWorkouts(workoutList : MutableLiveData<List<Workout>>){
        val dbRef: DatabaseReference = FirebaseDatabase.getInstance("https://myfitv3-760b1-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(_currentEmail).child("workouts")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {

                    val _workoutList : List<Workout> = snapshot.children.map { dataSnapshot ->

                        dataSnapshot.getValue(Workout::class.java)!!
                    }

                    workoutList.postValue(_workoutList)


                }catch (e : Exception){
                    Log.d("dbRef",e.message.toString());
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })

    }



}