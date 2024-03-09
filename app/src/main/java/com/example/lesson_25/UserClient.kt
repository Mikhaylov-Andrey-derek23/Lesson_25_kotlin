package com.example.lesson_25


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object UserClient {
    private var retrofit: Retrofit? = null

    fun getUsersService(): UsersService {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://reqres.in")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
        return retrofit!!.create(UsersService::class.java)

    }
}