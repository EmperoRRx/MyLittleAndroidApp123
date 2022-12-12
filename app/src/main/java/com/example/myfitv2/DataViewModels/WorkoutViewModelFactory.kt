package com.example.myfitv2.DataClasses


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myfitv2.DataViewModels.WorkoutViewModel


class ExtraParamsViewModelFactoryForWorkout(

    private val currentEmail: String
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        WorkoutViewModel(currentEmail) as T
}