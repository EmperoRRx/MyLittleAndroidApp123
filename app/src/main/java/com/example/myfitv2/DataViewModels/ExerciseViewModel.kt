package com.example.myfitv2.DataViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfitv2.DataClasses.Exercise
import com.example.myfitv2.Repository.ExerciseRepository


class ExerciseViewModel (currentWorkout : String,currentEmail : String) : ViewModel() {

    private val _allExercise= MutableLiveData<List<Exercise>>()
    val allExercise : LiveData<List<Exercise>> = _allExercise
    private var _currentWorkout : String = currentWorkout
    private var _currentEmail : String = currentEmail



    init {
        try {
            val repository = ExerciseRepository(_currentWorkout,_currentEmail).getInstance(_currentWorkout,_currentEmail)
            repository.loadWorkouts(_allExercise)

        } catch (e : Exception) {
            Log.d("ExerciseViewModel Error",e.message.toString());
        }

    }
}