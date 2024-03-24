package com.example.lesson_25

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lesson_25.main.view.MainFragment

//Темы: Работы с сетью.

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, MainFragment())
            .commit()
    }
}