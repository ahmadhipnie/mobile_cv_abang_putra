package com.pinaaa.cvabangputra.admin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.admin.DataFeedbackItem
import com.pinaaa.cvabangputra.data.remote.response.admin.FeedbackAdminResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.BarangResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataBarangItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BarangAdminViewModel : ViewModel() {


    private val _barangList = MutableLiveData<List<DataBarangItem>>()
    val barangList: LiveData<List<DataBarangItem>> = _barangList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchBarangs() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getBarangAdmin()
        client.enqueue(object : Callback<BarangResponse> {
            override fun onResponse(
                call: Call<BarangResponse>,
                response: Response<BarangResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val feedbackResponse = response.body()
                    Log.d("FeedbackViewModel", "Response body: $feedbackResponse")
                    feedbackResponse?.let {
                        _barangList.value = it.dataBarang
                    }
                } else {
                    Log.e("FeedbackViewModel", "Error Response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<BarangResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("FeedbackViewModel", "onFailure: ${t.message}", t)
            }
        })
    }

    fun searchBarangByName(namaBarang: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getBarangsBySearchAdmin(namaBarang)
        client.enqueue(object : Callback<BarangResponse> {
            override fun onResponse(call: Call<BarangResponse>, response: Response<BarangResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val barangResponse = response.body()
                    barangResponse?.let {
                        _barangList.value = it.dataBarang
                    }
                } else {
                    Log.e("BarangAdminViewModel", "Search Error Response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<BarangResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("BarangAdminViewModel", "Search Failure: ${t.message}", t)
            }
        })
    }


}