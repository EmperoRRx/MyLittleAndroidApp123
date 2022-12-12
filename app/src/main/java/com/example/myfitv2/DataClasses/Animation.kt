package com.example.myfitv2.DataClasses

import androidx.annotation.AnimRes

data class Animation(
    @AnimRes val enter: Int,
    @AnimRes val exit: Int,
    @AnimRes val popEnter: Int,
    @AnimRes val popExit: Int,
)