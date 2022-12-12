package com.example.myfitv2.Fragments



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitv2.Adapters.WorkoutAdapter
import com.example.myfitv2.DataClasses.*
import com.example.myfitv2.DataViewModels.WorkoutViewModel
import com.example.myfitv2.R




private lateinit var workoutRecyclerView : RecyclerView
private lateinit var workoutAdapter : WorkoutAdapter

class WorkoutFragment : Fragment(){


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {return inflater.inflate(R.layout.fragment_workout, container, false)}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            workoutRecyclerView = view.findViewById(R.id.RecyclerViewForWorkout)
            workoutRecyclerView.layoutManager = LinearLayoutManager(context)
            workoutRecyclerView.setHasFixedSize(true)

            val currentEmail = arguments?.get("currentEmail").toString()

            workoutAdapter = WorkoutAdapter(currentEmail)
            workoutRecyclerView.adapter = workoutAdapter

            val viewModel: WorkoutViewModel by viewModels {ExtraParamsViewModelFactoryForWorkout(currentEmail)}
            viewModel.allWorkouts.observe(viewLifecycleOwner) { workoutAdapter.updateWorkoutList(it) }

        } catch (e : Exception) { Log.d("Workout fragment ",e.message.toString()); }
    }
}

