package com.pinaaa.cvabangputra.data.remote

import androidx.lifecycle.LiveData
import com.pinaaa.cvabangputra.data.remote.response.LoginResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.BarangResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataItem
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

    @GET("getAllKategori")
    fun getKategori(): Call<KategoriResponse>


    @GET("getAllBarang")
    fun getBarang(): Call<BarangResponse>

}