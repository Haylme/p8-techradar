package com.example.techradar.retrofit

import Curencies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Locale

object CallApi {

    suspend fun fetchTranslateData(date: String?, to: Int?): Curencies {
        val service: ApiService = ApiService.retrofit.create(ApiService::class.java)
        return withContext(Dispatchers.IO) {

            if (date.isNullOrEmpty() || to == null) {
                throw IllegalArgumentException("date and to must be not null")
            }
        /**    if(getSystemLanguage() == "fr"){

                val sdf = SimpleDateFormat("dd/MM/yyyy")
                 sdf.parse(date)
               to = Currency.getInstance(Locale.FRANCE).numericCode


            }**/
            val currencies = Curencies(date, to)
            val response = service.getValue(currencies)

            response ?: throw IllegalStateException("Received null response body")
        }
    }

    private fun getSystemLanguage(): String {

        return Locale.getDefault().language


    }
}





