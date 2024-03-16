package com.example.lesson_25.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lesson_25.retrofit.Post

@Entity(
    tableName = "PostsDB"
)
data class PostDB(
    @PrimaryKey(autoGenerate = true) val id:Int? = null,
    val url:String,
    val title:String,
    val description:String
)

fun PostDB.toPost() = Post(
    url = url,
    title = title,
    description = description
)

