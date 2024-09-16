package com.example.currencyconversion

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AwsRetrofitClient {
    private const val BASE_URL = "http://16.16.26.242:3000/" // replace with your EC2 IP address

    val retrofitService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
