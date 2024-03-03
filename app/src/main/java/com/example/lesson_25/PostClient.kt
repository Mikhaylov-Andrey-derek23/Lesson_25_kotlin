package com.example.lesson_25

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object PostClient {
    private var retrofit: Retrofit? = null

    fun getPostsService(): PostService {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://api.slingacademy.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
        return retrofit!!.create(PostService::class.java)

    }
}