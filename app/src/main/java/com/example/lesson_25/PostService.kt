package com.example.lesson_25

import retrofit2.http.GET

interface PostService {

    @GET("v1/sample-data/photos")
    suspend fun getPicture():MainPost
}