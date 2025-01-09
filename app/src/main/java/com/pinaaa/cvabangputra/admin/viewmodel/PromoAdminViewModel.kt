package com.pinaaa.cvabangputra.admin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.admin.DataPromoItem
import com.pinaaa.cvabangputra.data.remote.response.admin.PromoAdminResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromoAdminViewModel : ViewModel() {

    private val _promoList = MutableLiveData<List<DataPromoItem>>()
    val promoList: LiveData<List<DataPromoItem>> = _promoList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

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

}



