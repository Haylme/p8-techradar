package com.example.techradar.retrofit

import Curencies
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET

interface ApiService {

    @GET("/currencies")
    suspend fun getValue(
        @Body curencies: Curencies

    ):Curencies?


    companion object {

        const val api = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/{{from}}/{{to}}.json"

        val retrofit:Retrofit = Retrofit.Builder()
            .baseUrl(api)
            .build()



    }


}