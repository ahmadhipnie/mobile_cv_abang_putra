package com.pinaaa.cvabangputra.data.remote

import com.pinaaa.cvabangputra.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiConfig {

    var URL = "https://53d8-140-213-118-23.ngrok-free.app/"

    var api = "api/"

    var URL_API = URL + api

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