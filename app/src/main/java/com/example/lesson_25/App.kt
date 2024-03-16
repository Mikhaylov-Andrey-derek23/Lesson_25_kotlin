package com.example.lesson_25

import android.app.Application
import com.example.lesson_25.room.AppDataBase
import com.example.lesson_25.room.PostDao

class App:Application() {

    var database: AppDataBase? = null
    var postDao: PostDao? = null

    override fun onCreate() {
        super.onCreate()
        database = AppDataBase.getDatabase(this)
        postDao = database?.getPostDao()
    }
}