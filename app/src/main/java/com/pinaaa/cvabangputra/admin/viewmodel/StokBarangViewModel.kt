package com.pinaaa.cvabangputra.admin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.FeedbackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StokBarangViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _updateStok = MutableLiveData<Boolean>()
    val updateStok: LiveData<Boolean> = _updateStok


    fun updateStokBarang(idBarang: Int, stokBarang: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().updateStokBarang(idBarang, stokBarang)
        client.enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(
                call: Call<FeedbackResponse>,
                response: Response<FeedbackResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _updateStok.value = true
                    Log.d("StokBarangAdminViewModel", "stok Barang successfully")
                } else {
                    _updateStok.value = false
                    Log.e("StokBarangAdminViewModel", "update failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                _isLoading.value = false
                _updateStok.value = false
                Log.e("StokBarangAdminViewModel", "update failure: ${t.message}", t)
            }
        })
    }
}