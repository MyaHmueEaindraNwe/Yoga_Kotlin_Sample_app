package com.example.yogaapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class YogaClass(
    var classId: Int,
    var courseId: Int,
    var dateOfClass: String,
    var teacher: String,
    var comments: String
) : Parcelable
