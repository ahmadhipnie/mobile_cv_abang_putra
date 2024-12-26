package com.pinaaa.cvabangputra.di

import android.content.Context
import com.pinaaa.cvabangputra.Repository
import com.pinaaa.cvabangputra.data.remote.ApiConfig

object injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig().getApiService()
        return Repository.getInstance(apiService)
    }

    fun rupiahFormat(price: Int): String {
        val formattedPrice = StringBuilder()
        val priceString = price.toString()
        val priceLength = priceString.length
        var counter = 0
        for (i in priceLength - 1 downTo 0) {
            formattedPrice.append(priceString[i])
            counter++
            if (counter % 3 == 0 && i != 0) {
                formattedPrice.append(".")
            }
        }
        return "Rp ${formattedPrice.reverse()}"
    }
}