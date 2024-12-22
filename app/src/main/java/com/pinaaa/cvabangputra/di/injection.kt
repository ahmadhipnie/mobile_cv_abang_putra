package com.pinaaa.cvabangputra.di

import android.content.Context
import com.pinaaa.cvabangputra.Repository
import com.pinaaa.cvabangputra.data.remote.ApiConfig

object injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig().getApiService()
        return Repository.getInstance(apiService)
    }
}