package com.example.myfitv2.DataClasses

data class User (
    var userId: String? = null,
    var userName : String? = null,
    var userSurname : String? = null,
    var userGender : String? = null,
    var userEmail : String? = null,
    var userAge : Int? = null,
    var loggedIn : Boolean? = null,
    var new : Boolean? = null,
    var workouts : `1Workouts`? = null,
    var events : `1Events`? = null,
    var friends : `1Friends`? = null
)

