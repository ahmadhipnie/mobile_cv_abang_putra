package com.pinaaa.cvabangputra

import com.pinaaa.cvabangputra.data.remote.ApiService
import com.pinaaa.cvabangputra.data.remote.response.LoginResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.BarangResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataBarangItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataGambarBarangItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataGambarPromoItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.GambarBarangResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.KategoriResponse
import retrofit2.Call
import retrofit2.awaitResponse


class Repository private constructor(private val apiService: ApiService) {


    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun getKategori(): List<DataItem> {
        // Pemanggilan API secara langsung menggunakan suspend function
        val response = apiService.getKategori()
        return response.listData?.filterNotNull() ?: emptyList()  // Mengambil data kategori
    }

    suspend fun getBarang(): List<DataBarangItem> {
        // Pemanggilan API secara langsung menggunakan suspend function
        val response = apiService.getBarang()
        return response.dataBarang?.filterNotNull() ?: emptyList()  // Mengambil data barang
    }

    suspend fun getBarangByKategori(kategoriId: Int): List<DataBarangItem> {
        // Pemanggilan API secara langsung menggunakan suspend function
        val response = apiService.getBarangByKategori(kategoriId)  // Memanggil API
        return response.dataBarang?.filterNotNull() ?: emptyList()  // Mengambil data barang, filter null values
    }

    suspend fun getBarangsBySearch(namaBarang: String, kategoriId: Int ): List<DataBarangItem> {
        // Pemanggilan API secara langsung menggunakan suspend function
        val response = apiService.getBarangsBySearch(namaBarang, kategoriId)  // Memanggil API
        return response.dataBarang?.filterNotNull() ?: emptyList()  // Mengambil data barang, filter null values
    }

    suspend fun getImagesBarangByIdBarang(id_barang: Int): List<DataGambarBarangItem> {
        val gambarBarangResponse = apiService.getImagesBarangByIdBarang(id_barang)
        return gambarBarangResponse.dataGambarBarang?.filterNotNull() ?: emptyList()
    }

    suspend fun getAllGambarPromo(): List<DataGambarPromoItem> {
        val gambarPromoResponse = apiService.getAllGambarPromo()
        return gambarPromoResponse.dataGambarPromo?.filterNotNull() ?: emptyList()
    }


    companion object {
        fun getInstance(
            apiService: ApiService,
        ) = Repository(apiService)
    }
}