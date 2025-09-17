package com.example.yogaapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class YogaCourse(
    val id: Int=0,
    val dayOfWeek: String="",
    val timeOfCourse: String="",
    val capacity: Int=0,
    val duration: Int=0,
    val price: Int=0,
    val typeOfClass: String="",
    val description: String=""
) : Parcelable
