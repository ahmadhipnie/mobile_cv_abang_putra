package com.pinaaa.cvabangputra.data.remote

import androidx.lifecycle.LiveData
import com.pinaaa.cvabangputra.data.remote.response.LoginResponse
import com.pinaaa.cvabangputra.data.remote.response.admin.FeedbackAdminResponse
import com.pinaaa.cvabangputra.data.remote.response.admin.PromoAdminResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.BarangResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.FeedbackResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.GambarBarangResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.GambarPromoResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.KategoriResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

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



    @FormUrlEncoded
    @POST("getImagesPromoByIdPromo")
    suspend fun getImagesPromoByIdPromo(
        @Field("id_promo") id_promo: Int
    ): GambarPromoResponse


    @GET("getAllGambarPromo")
    suspend fun getAllGambarPromo(): GambarPromoResponse

    @GET("getAllKategori")
    suspend fun getKategori(): KategoriResponse

    @GET("getAllKategori")
    fun getKategoriAdmin(): Call<KategoriResponse>


    @GET("getAllBarang")
    suspend fun getBarang(): BarangResponse

    @GET("getAllBarang")
    fun getBarangAdmin(): Call<BarangResponse>

    @GET("getAllPromo")
    fun getPromoAdmin(): Call<PromoAdminResponse>

    @GET("getAllFeedback")
    fun getAllFeedback(): Call<FeedbackAdminResponse>

    @FormUrlEncoded
    @POST("getBarangsByKategori")
    fun getBarangByKategori(
        @Field("kategori_id") kategori_id: Int
    ): Call<BarangResponse>

    @FormUrlEncoded
    @POST("kirimFeedbackReseller")
    suspend fun kirimFeedbackReseller(
        @Field("email") email: String,
        @Field("nama_reseller") nama_reseller: String,
        @Field("rating") rating: Int,
        @Field("isi_feedback") isi_feedback: String,
    ): FeedbackResponse

    @Multipart
    @POST("addKategori")
    suspend fun addKategori(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photo: MultipartBody.Part? = null
    ): FeedbackResponse

    @Multipart
    @POST("addBarang")
    fun addBarang(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part gambarUrl1: MultipartBody.Part? = null,
        @Part gambarUrl2: MultipartBody.Part? = null,
        @Part gambarUrl3: MultipartBody.Part? = null
    ): Call<FeedbackResponse>

    @Multipart
    @POST("addPromo")
    fun addPromo(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part gambarUrl1: MultipartBody.Part? = null,
        @Part gambarUrl2: MultipartBody.Part? = null,
        @Part gambarUrl3: MultipartBody.Part? = null
    ): Call<FeedbackResponse>


    @Multipart
    @POST("updateUserReseller")
    suspend fun updateUserReseller(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part photo: MultipartBody.Part? = null
    ): FeedbackResponse


    @FormUrlEncoded
    @POST("getBarangsBySearch")
    fun getBarangsBySearch(
        @Field("nama_barang") nama_barang: String,
        @Field("kategori_id") kategori_id: Int
    ): Call<BarangResponse>

    @FormUrlEncoded
    @POST("getBarangsBySearchAdmin")
    fun getBarangsBySearchAdmin(
        @Field("nama_barang") nama_barang: String
    ): Call<BarangResponse>


    @FormUrlEncoded
    @POST("deletePromo")
    fun deletePromo(
        @Field("id_promo") id_promo: Int
    ): Call<FeedbackResponse>

    @FormUrlEncoded
    @POST("deleteBarang")
    fun deleteBarang(
        @Field("id_barang") id_barang: Int
    ): Call<FeedbackResponse>

    @FormUrlEncoded
    @POST("updatePassword")
    fun updatePassword(
        @Field("id_user") id_user: Int,
        @Field("password") password: String
    ): Call<FeedbackResponse>

    @FormUrlEncoded
    @POST("addUserAndReseller")
    fun addUserAndReseller(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("nama") nama: String,
        @Field("nomor_telepon") nomor_telepon: String,
        @Field("tanggal_lahir") tanggal_lahir: String,
    ): Call<FeedbackResponse>

    @FormUrlEncoded
    @POST("updateStokBarang")
    fun updateStokBarang(
        @Field("id_barang") id_barang: Int,
        @Field("stok_barang") stok_barang: Int
    ): Call<FeedbackResponse>

    @FormUrlEncoded
    @POST("getPromosBySearchAdmin")
    fun getPromosBySearchAdmin(
        @Field("nama_promo") nama_barang: String
    ): Call<PromoAdminResponse>

}