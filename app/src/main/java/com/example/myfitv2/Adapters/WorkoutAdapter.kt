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
import com.example.myfitv2.*
import com.example.myfitv2.Activities.ShowAllExercises
import com.example.myfitv2.Fragments.makeInVisible
import com.example.myfitv2.DataClasses.Workout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class WorkoutAdapter(private var currentEmail: String) : RecyclerView.Adapter<WorkoutAdapter.MyViewHolder>() {
    private val workoutList = ArrayList<Workout>()
    private var cnt  : Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.workout_item,
            parent,false
        )

        cnt += 1
        return MyViewHolder(itemView,cnt,workoutList,currentEmail)
    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val workout = workoutList[position]
        holder.workoutName.text = workout.name.toString()
    }

    override fun getItemCount(): Int {return workoutList.size}

    @SuppressLint("NotifyDataSetChanged")
    fun updateWorkoutList(workoutList : List<Workout>) {
        this.workoutList.clear()
        this.workoutList.addAll(workoutList)
        notifyDataSetChanged()
    }


    class  MyViewHolder(itemView : View, cnt : Int, var list: ArrayList<Workout>, currentEmail: String) : RecyclerView.ViewHolder(itemView){

        val workoutName : TextView = itemView.findViewById(R.id.HolderForFriendName)
        private val openExercises : Button =  itemView.findViewById(R.id.openCalendarBt)
        private val deleteWorkout : Button =  itemView.findViewById(R.id.openWorkoutsBt)


        var mAuth: FirebaseAuth? = null
        var dbRef: DatabaseReference


        init {
            mAuth = FirebaseAuth.getInstance()
            val user = mAuth?.currentUser?.email.toString().replace(".", "")
            dbRef = FirebaseDatabase.getInstance("https://myfitv3-760b1-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(currentEmail.replace(".", "")).child("workouts").child(list[cnt].name.toString())

            if (user != currentEmail.replace(".", "")) {deleteWorkout.makeInVisible()}



            openExercises.setOnClickListener {
                try {
                    val context = itemView.context
                    val intent = Intent(context, ShowAllExercises::class.java)
                    intent.putExtra("currentWorkout",workoutName.text.toString())
                    intent.putExtra("currentEmail",currentEmail)
                    context.startActivity(intent)
                } catch (e : Exception) {
                    Log.d("Error trans ",e.message.toString());
                }
            }

            deleteWorkout.setOnClickListener {dbRef.removeValue()}
        }

    }
}