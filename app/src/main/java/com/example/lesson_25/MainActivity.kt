package com.example.lesson_25

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//Темы: Работы с сетью.

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, FirstFragment())
            .commit()
    }
}