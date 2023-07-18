package com.example.simpleviralgamestask

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DogApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dog.ceo/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: DogApiService by lazy {
        retrofit.create(DogApiService::class.java)
    }
}