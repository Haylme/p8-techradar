package com.example.techradar.retrofit

import Curencies
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {

    @GET("currencies/eur.json")
    suspend fun getValue():Response<Curencies>


    companion object {

      const val api = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/"



        val retrofit:Retrofit = Retrofit.Builder()
            .baseUrl(api)
            .addConverterFactory(GsonConverterFactory.create())

            .build()




    }


}