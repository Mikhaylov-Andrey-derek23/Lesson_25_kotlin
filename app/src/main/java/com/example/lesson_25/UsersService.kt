package com.example.lesson_25

import retrofit2.http.GET
import retrofit2.http.Query

interface UsersService {
    @GET("api/users")

    suspend fun getPicture(@Query("page") page:Int):MainUsers

}
