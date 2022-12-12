package com.example.myfitv2.DataClasses

import java.time.LocalDate

data class Event(var id: String ?= null, var text: String ?= null, var date: LocalDate ?= null)