package com.example.myfitv2.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myfitv2.DataClasses.Friend
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FriendsRepository {

    var mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    var  user = mAuth?.currentUser?.email.toString().replace(".", "")
    private  var dbRef: DatabaseReference = FirebaseDatabase.getInstance("https://myfitv3-760b1-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(user).child("friends")

    @Volatile private  var INSTANCE : FriendsRepository ?= null

    fun getInstance() : FriendsRepository{
        return INSTANCE ?: synchronized(this){

            val instance = FriendsRepository()
            INSTANCE = instance
            instance
        }
    }


    fun loadFriends(friendList : MutableLiveData<List<Friend>>){

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {

                    val _friendList : List<Friend> = snapshot.children.map { dataSnapshot ->

                        dataSnapshot.getValue(Friend::class.java)!!
                    }

                    friendList.postValue(_friendList)


                }catch (e : Exception){
                    Log.d("dbRef",e.message.toString());
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }



}