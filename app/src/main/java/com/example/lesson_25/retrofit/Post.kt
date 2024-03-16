package com.example.lesson_25.retrofit

import com.example.lesson_25.room.PostDB

data class Post(
    val url:String,
    val title:String,
    val description:String

)

fun Post.toPostDB() = PostDB(
    url = url,
    title = title,
    description = description
)