package com.example.myfitv2.DataClasses

data class Exercise (var exerciseName : String ?= null, var reps : Int? = null,var series : Int? = null, var startingWeight : Double? = null,var maxWeight : Double? = null)