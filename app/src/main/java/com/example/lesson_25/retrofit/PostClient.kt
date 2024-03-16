package com.example.lesson_25.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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