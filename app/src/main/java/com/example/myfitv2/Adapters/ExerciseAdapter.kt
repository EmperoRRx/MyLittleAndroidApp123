package com.example.myfitv2.Adapters
//##68ED13
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitv2.Fragments.makeInVisible
import com.example.myfitv2.DataClasses.Exercise
import com.example.myfitv2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ExerciseAdapter (private val currentWorkout: String, private val currentEmail: String) : RecyclerView.Adapter<ExerciseAdapter.MyViewHolder>() {

    private val exerciseList = ArrayList<Exercise>()
    private var cnt  : Int = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.exercise_item,
            parent,false
        )

        cnt += 1
        return MyViewHolder(itemView,currentWorkout,currentEmail,cnt,exerciseList)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
5
        Log.d("Exercise list position",position.toString());
        val exercise = exerciseList[position]

        holder.exerciseName.text = exercise.exerciseName.toString()
        holder.exerciseReps.text = exercise.reps.toString()
        holder.exerciseSeries.text = exercise.series.toString()
        holder.exerciseStart.text = exercise.startingWeight.toString()
        holder.exerciseMax.text = exercise.maxWeight.toString()

    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun UpdateExerciseList(exerciseList : List<Exercise>) {

        this.exerciseList.clear()
        this.exerciseList.addAll(exerciseList)
        notifyDataSetChanged()
    }




    class  MyViewHolder(itemView : View,currentWorkout : String,currentEmail : String,cnt : Int,list : ArrayList<Exercise>) : RecyclerView.ViewHolder(itemView){


        val exerciseName    : TextView = itemView.findViewById(R.id.tvExercise)
        val exerciseSeries  : TextView = itemView.findViewById(R.id.tvSeries)
        val exerciseReps    : TextView = itemView.findViewById(R.id.tvReps)
        val exerciseMax     : TextView = itemView.findViewById(R.id.tvMax)
        val exerciseStart   : TextView = itemView.findViewById(R.id.tvStart)


        private val exerciseRemove          : Button = itemView.findViewById(R.id.changeButton1)
        private val exerciseSeriesChange    : Button = itemView.findViewById(R.id.changeButton2)
        private val exerciseRepsChange      : Button = itemView.findViewById(R.id.changeButton3)
        private val exerciseStartChange     : Button = itemView.findViewById(R.id.changeButton4)
        private val exerciseMaxChange       : Button = itemView.findViewById(R.id.changeButton5)
        private val doneButton              : Button = itemView.findViewById(R.id.doneButton)

        private val backgroundCard          : CardView = itemView.findViewById(R.id.exerciseFragmentBackground)



        var mAuth: FirebaseAuth? = null
        var dbRef: DatabaseReference


        init {
            mAuth = FirebaseAuth.getInstance()
            val user = mAuth?.currentUser?.email.toString().replace(".", "")

            dbRef = FirebaseDatabase.getInstance("https://myfitv3-760b1-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users").child(currentEmail.replace(".", "")).child("workouts").child(currentWorkout).child("exercises").child(list[cnt].exerciseName.toString())

            if (user != currentEmail.replace(".", "")) {
                exerciseRemove.makeInVisible()
                exerciseSeriesChange.makeInVisible()
                exerciseRepsChange.makeInVisible()
                exerciseStartChange.makeInVisible()
                exerciseMaxChange.makeInVisible()
                doneButton.makeInVisible()

                exerciseName.isEnabled = false
                exerciseSeries.isEnabled = false
                exerciseReps.isEnabled = false
                exerciseMax.isEnabled = false
                exerciseStart.isEnabled = false
            }
            exerciseRemove.setOnClickListener{
                dbRef.removeValue()
            }

            doneButton.setOnClickListener{
                backgroundCard.setCardBackgroundColor(R.drawable.green)
            }

            exerciseSeriesChange.setOnClickListener{
                try {
                    var check = exerciseSeries.text.toString().toInt()
                    dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val map: MutableMap<String, Any> = HashMap()
                            map["series"] = exerciseSeries.text.toString().toInt()
                            dbRef.ref.updateChildren(map)
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                } catch (e : Exception) {
                    exerciseSeries.error = "Incorrect series format"
                }
            }

            exerciseRepsChange.setOnClickListener{
                try {
                    var check = exerciseReps.text.toString().toInt()
                    dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val map: MutableMap<String, Any> = HashMap()
                            map["reps"] = exerciseReps.text.toString().toInt()
                            dbRef.ref.updateChildren(map)
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                } catch (e : Exception) {
                    exerciseReps.error = "Incorrect reps format"
                }
            }

            exerciseMaxChange.setOnClickListener{
                try {
                    var check = exerciseMax.text.toString().toDouble()
                    dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val map: MutableMap<String, Any> = HashMap()
                            map["maxWeight"] = exerciseMax.text.toString().toDouble()
                            dbRef.ref.updateChildren(map)
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                } catch (e : Exception) {
                    exerciseMax.error = "Incorrect max weight format"
                }
            }

            exerciseStartChange.setOnClickListener{
                try {
                    var check = exerciseStart.text.toString().toDouble()
                    dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val map: MutableMap<String, Any> = HashMap()
                            map["startingWeight"] = exerciseStart.text.toString().toDouble()
                            dbRef.ref.updateChildren(map)
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                } catch (e : Exception) {
                    exerciseStart.error = "Incorrect starting weight format"
                }
            }

        }
    }
}