package com.example.lesson_25.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lesson_25.retrofit.Post


@Dao
interface PostDao {

    @Insert
    fun savePosts(post: List<PostDB>)

    @Query("Select * from PostsDB")
    fun getPosts(): List<PostDB>
}