package com.example.currencyconversion

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    // Define the data endpoint ("/api/data")
    @GET("/api/data")
    fun getData(): Call<ApiResponse> // Change this to return an ApiResponse
}
