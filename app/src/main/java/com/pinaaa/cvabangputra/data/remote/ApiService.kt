package com.pinaaa.cvabangputra.data.remote

import androidx.lifecycle.LiveData
import com.pinaaa.cvabangputra.data.remote.response.LoginResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.BarangResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.GambarBarangResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.GambarPromoResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.KategoriResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse


    @FormUrlEncoded
    @POST("getImagesBarangByIdBarang")
    suspend fun getImagesBarangByIdBarang(
        @Field("id_barang") id_barang: Int
    ): GambarBarangResponse


    @GET("getAllGambarPromo")
    suspend fun getAllGambarPromo(): GambarPromoResponse

    @GET("getAllKategori")
    suspend fun getKategori(): KategoriResponse




    @GET("getAllBarang")
    suspend fun getBarang(): BarangResponse

    @FormUrlEncoded
    @POST("getBarangsByKategori")
    suspend fun getBarangByKategori(
        @Field("kategori_id") kategori_id: Int
    ): BarangResponse

    @FormUrlEncoded
    @POST("getBarangsBySearch")
    suspend fun getBarangsBySearch(
        @Field("nama_barang") nama_barang: String,
        @Field("kategori_id") kategori_id: Int
    ): BarangResponse

}