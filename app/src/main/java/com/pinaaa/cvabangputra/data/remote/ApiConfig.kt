package com.pinaaa.cvabangputra.data.remote

import android.net.Uri
import com.pinaaa.cvabangputra.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiConfig {

    var URL_RAW = Uri.parse("http://192.168.70.23:8000/")

    var URL = URL_RAW.toString()

    var api = "api/"

    var URL_API = URL + api

    companion object {
        fun getApiService(): ApiService {
            val client = OkHttpClient.Builder()
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.70.23:8000/api/") // Sesuaikan base URL
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }


        fun getApiService(): ApiService {




            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .build()
                chain.proceed(requestHeaders)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()


            return retrofit.create(ApiService::class.java)
        }



}