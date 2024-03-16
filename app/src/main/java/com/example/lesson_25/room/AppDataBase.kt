package com.example.lesson_25.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.concurrent.Volatile

@Database(entities = [PostDB::class], version = 1)
abstract class AppDataBase:RoomDatabase() {

    abstract fun getPostDao():PostDao

    companion object {

        @Volatile
        private  var INSTANCE:AppDataBase? = null


        fun getDatabase(context: Context):AppDataBase {
            return  INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "post_database"
                )
                    .build()
                INSTANCE = instance

                instance
            }

        }
    }
}