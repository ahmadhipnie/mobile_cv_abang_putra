package com.pinaaa.cvabangputra

import com.pinaaa.cvabangputra.data.remote.ApiService
import com.pinaaa.cvabangputra.data.remote.response.LoginResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.BarangResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataBarangItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.KategoriResponse
import retrofit2.awaitResponse


class Repository private constructor(private val apiService: ApiService) {


    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun getKategori(): List<DataItem> {
        val response = apiService.getKategori().awaitResponse()
        if (response.isSuccessful) {
            val kategoriResponse: KategoriResponse? = response.body()
            return kategoriResponse?.listData?.filterNotNull() ?: emptyList()
        } else {
            throw Exception("Failed to fetch categories: ${response.code()} ${response.message()}")
        }
    }

    suspend fun getBarang(): List<DataBarangItem> {
        val response = apiService.getBarang().awaitResponse()
        if (response.isSuccessful) {
            val barangResponse: BarangResponse? = response.body()
            return barangResponse?.dataBarang?.filterNotNull() ?: emptyList()
        } else {
            throw Exception("Failed to fetch barangs: ${response.code()} ${response.message()}")
        }
    }


    companion object {
        fun getInstance(
            apiService: ApiService,
        ) = Repository(apiService)
    }
}