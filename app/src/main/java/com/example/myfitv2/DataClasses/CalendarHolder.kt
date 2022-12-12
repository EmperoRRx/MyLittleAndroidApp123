package com.example.myfitv2.DataClasses


import com.example.myfitv2.Fragments.BaseFragment

data class CalendarHolder(val titleRes: String?, val subtitleRes: String, val animation: Animation, val createView: () -> BaseFragment)




