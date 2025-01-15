package com.pinaaa.cvabangputra.admin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.admin.DataPromoItem
import com.pinaaa.cvabangputra.data.remote.response.admin.PromoAdminResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.FeedbackResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Date

class PromoAdminViewModel : ViewModel() {

    private val _promoList = MutableLiveData<List<DataPromoItem>>()
    val promoList: LiveData<List<DataPromoItem>> = _promoList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _deletePromoStatus = MutableLiveData<Boolean>()
    val deletePromoStatus: LiveData<Boolean> = _deletePromoStatus

    private val _feedbackResponse = MutableLiveData<FeedbackResponse>()
    val feedbackResponse: LiveData<FeedbackResponse> = _feedbackResponse

    fun fetchPromos() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getPromoAdmin()
        client.enqueue(object : Callback<PromoAdminResponse> {
            override fun onResponse(
                call: Call<PromoAdminResponse>,
                response: Response<PromoAdminResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val feedbackResponse = response.body()
                    Log.d("FeedbackViewModel", "Response body: $feedbackResponse")
                    feedbackResponse?.let {
                        _promoList.value = it.dataPromo
                    }
                } else {
                    Log.e("FeedbackViewModel", "Error Response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PromoAdminResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("FeedbackViewModel", "onFailure: ${t.message}", t)
            }
        })
    }

    fun searchPromoByName(namaPromo: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getPromosBySearchAdmin(namaPromo)
        client.enqueue(object : Callback<PromoAdminResponse> {
            override fun onResponse(
                call: Call<PromoAdminResponse>,
                response: Response<PromoAdminResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val promoResponse = response.body()
                    promoResponse?.let {
                        _promoList.value = it.dataPromo
                    }
                } else {
                    Log.e(
                        "PromoAdminViewModel",
                        "Search Error Response: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<PromoAdminResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("PromoAdminViewModel", "Search Failure: ${t.message}", t)
            }
        })
    }

    fun deletePromo(idPromo: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().deletePromo(idPromo)
        client.enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(
                call: Call<FeedbackResponse>,
                response: Response<FeedbackResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _deletePromoStatus.value = true
                    Log.d("PromoAdminViewModel", "Promo deleted successfully")
                } else {
                    _deletePromoStatus.value = false
                    Log.e("PromoAdminViewModel", "Delete failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                _isLoading.value = false
                _deletePromoStatus.value = false
                Log.e("PromoAdminViewModel", "Delete failure: ${t.message}", t)
            }
        })
    }

    fun addPromo(
        namaPromo: String, deskripsiPromo : String, tanggalPeriodeAwal : String , tanggalPeriodeAkhir : String ,
        gambar1: File?, gambar2: File?, gambar3: File?
    ) {
        _isLoading.value = true

        // Prepare RequestBody for other data (non-file)
        val namaPromoRequest = RequestBody.create("text/plain".toMediaTypeOrNull(), namaPromo)
        val deskripsiPromoRequest = RequestBody.create("text/plain".toMediaTypeOrNull(), deskripsiPromo)
        val tanggalPeriodeAwalRequest = RequestBody.create("text/plain".toMediaTypeOrNull(), tanggalPeriodeAwal)
        val tanggalPeriodeAkhirRequest = RequestBody.create("text/plain".toMediaTypeOrNull(), tanggalPeriodeAkhir)

        // Prepare MultipartBody.Part for images
        val gambarUrl1Part = gambar1?.let {
            val file = File(it.path!!)
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("gambar_url_1", file.name, requestBody)
        }

        val gambarUrl2Part = gambar2?.let {
            val file = File(it.path!!)
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("gambar_url_2", file.name, requestBody)
        }

        val gambarUrl3Part = gambar3?.let {
            val file = File(it.path!!)
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("gambar_url_3", file.name, requestBody)
        }

        val params = mutableMapOf<String, RequestBody>()
        params["nama_promo"] = namaPromoRequest
        params["deskripsi_promo"] = deskripsiPromoRequest
        params["tanggal_periode_awal"] = tanggalPeriodeAwalRequest
        params["tanggal_periode_akhir"] = tanggalPeriodeAkhirRequest

        // Call API to add Barang
        val client = ApiConfig.getApiService().addPromo(params, gambarUrl1Part, gambarUrl2Part, gambarUrl3Part)
        client.enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(call: Call<FeedbackResponse>, response: Response<FeedbackResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _feedbackResponse.value = response.body()
                } else {
                    Log.e("PromoAdminViewModel", "Failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("PromoAdminViewModel", "onFailure: ${t.message}", t)
            }
        })
    }

}



