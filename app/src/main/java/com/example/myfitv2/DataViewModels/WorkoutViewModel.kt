package com.example.myfitv2.DataViewModels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfitv2.DataClasses.Workout
import com.example.myfitv2.Repository.WorkoutRepository

class WorkoutViewModel(currentEmail : String) : ViewModel() {

   // private val repository : WorkoutRepository
    private val _currentEmail = currentEmail
    private val _allWorkouts= MutableLiveData<List<Workout>>()
    val allWorkouts : LiveData<List<Workout>> = _allWorkouts


    init {
       // repository = WorkoutRepository().getInstance()
       // repository.loadWorkouts(_allWorkouts)
        val repository = WorkoutRepository(_currentEmail).getInstance(_currentEmail)
        repository.loadWorkouts(_allWorkouts)
    }
}