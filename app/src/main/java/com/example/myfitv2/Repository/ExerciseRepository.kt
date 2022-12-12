package com.example.myfitv2.Repository


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myfitv2.DataClasses.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ExerciseRepository (private var currentWorkout: String, private var currentEmail: String) {

    var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    var user = mAuth?.currentUser?.email.toString().replace(".", "")


    @Volatile private  var INSTANCE : ExerciseRepository ?= null

    fun getInstance(currentWorkout : String,currentEmail: String) : ExerciseRepository{
        return INSTANCE ?: synchronized(this){
            val instance = ExerciseRepository(currentWorkout,currentEmail)
            INSTANCE = instance
            instance
        }
    }


    fun loadWorkouts(exerciseList : MutableLiveData<List<Exercise>>){

        val dbRef: DatabaseReference = FirebaseDatabase.getInstance("https://myfitv3-760b1-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(currentEmail.toString().replace(".", "")).child("workouts").child(currentWorkout).child("exercises")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {

                    val _exerciseList : List<Exercise> = snapshot.children.map { dataSnapshot -> dataSnapshot.getValue(Exercise::class.java)!!}
                    exerciseList.postValue(_exerciseList)

                }catch (e : Exception){

                    Log.d("Exercise mapping error",e.message.toString());
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }



}