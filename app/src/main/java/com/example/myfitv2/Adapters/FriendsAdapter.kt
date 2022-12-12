package com.example.myfitv2.Adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitv2.Activities.ShowAllWorkouts
import com.example.myfitv2.DataClasses.Friend
import com.example.myfitv2.R

class FriendsAdapter : RecyclerView.Adapter<FriendsAdapter.MyViewHolder>() {
    private val friendList = ArrayList<Friend>()
    private var cnt  : Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.friend_item,
            parent,false
        )

        cnt += 1
        return MyViewHolder(itemView,cnt,friendList)
    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val friend = friendList[position]
        holder.friendName.text = friend.friendEmail.toString()

    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun UpdateFriendList(friendList : List<Friend>) {

        this.friendList.clear()
        this.friendList.addAll(friendList)
        notifyDataSetChanged()
    }


    class  MyViewHolder(itemView : View,cnt : Int,list : ArrayList<Friend>) : RecyclerView.ViewHolder(itemView){

        val friendName : TextView = itemView.findViewById(R.id.HolderForFriendName)
        val friendWorkout : Button  = itemView.findViewById(R.id.openWorkoutsBt)

        init {
            friendWorkout.setOnClickListener {
                try {
                    val context = itemView.context
                    val intent = Intent(context, ShowAllWorkouts::class.java)
                    intent.putExtra("email",friendName.text.toString())
                    context.startActivity(intent)
                } catch (e : Exception) {
                    Log.d("Error trans ",e.message.toString());
                }
            }
        }
    }
}

