package com.example.techradar.retrofit

import Curencies
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

object CallApi {

    suspend fun fetchTranslateData(to: Double?): Double {
        val service: ApiService = ApiService.retrofit.create(ApiService::class.java)
        return withContext(Dispatchers.IO) {

            if (to == null) {
                throw IllegalArgumentException("value is null")
            }

            val response: Response<Curencies> = service.getValue()
            if (response.isSuccessful) {
                val currencies =
                    response.body() ?: throw IllegalStateException("Received null response body")

                val gbpValue = currencies.eur.gbp


                val newValue = to * gbpValue
                newValue
            } else {
                Log.e(
                    "CallApi",
                    "Request failed with status: ${response.code()} and message: ${response.message()}"
                )
                throw IllegalStateException("Request failed with status: ${response.code()}")
            }
        }
    }
}