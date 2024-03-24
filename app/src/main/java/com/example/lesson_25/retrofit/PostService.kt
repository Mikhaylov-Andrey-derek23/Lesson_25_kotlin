package com.example.lesson_25.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface PostService {

    @GET("v1/sample-data/photos")

    suspend fun getPostForNetwork(@Query("limit") limit:Int, @Query("offset") offset:Int): MainPost
}