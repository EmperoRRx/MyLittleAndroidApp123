package com.example.myfitv2.Fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfitv2.Adapters.ExerciseAdapter
import com.example.myfitv2.DataViewModels.ExerciseViewModel
import com.example.myfitv2.DataClasses.ExtraParamsViewModelFactory
import com.example.myfitv2.R





private lateinit var exerciseRecyclerView : RecyclerView
lateinit var exerciseAdapter  : ExerciseAdapter


class ExerciseFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {return inflater.inflate(R.layout.fragment_exercise, container, false)}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            exerciseRecyclerView = view.findViewById(R.id.RecyclerViewForExercise)
            exerciseRecyclerView.layoutManager = LinearLayoutManager(context)
            exerciseRecyclerView.setHasFixedSize(true)

            val currentWorkout = arguments?.get("currentWorkout").toString()
            val currentEmail = arguments?.get("currentEmail").toString()

            exerciseAdapter = ExerciseAdapter(currentWorkout,currentEmail)
            exerciseRecyclerView.adapter = exerciseAdapter

            val viewModel: ExerciseViewModel by viewModels {ExtraParamsViewModelFactory(currentWorkout,currentEmail)}
            viewModel.allExercise.observe(viewLifecycleOwner, Observer(exerciseAdapter::UpdateExerciseList))

        } catch (e : Exception) {Log.d("ExerciseFragment Error",e.message.toString());}
    }
}

