package com.example.myfitv2.DataClasses


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfitv2.DataViewModels.ExerciseViewModel


class ExtraParamsViewModelFactory(

    private val currentWorkout: String,
    private val currentEmail: String,
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ExerciseViewModel(currentWorkout,currentEmail) as T
}