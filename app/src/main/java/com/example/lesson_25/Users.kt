package com.example.lesson_25

import com.google.gson.annotations.SerializedName

data class Users (
    @SerializedName("first_name") val first_name:String,
    val last_name: String,
    val email: String,
    val avatar: String,

)