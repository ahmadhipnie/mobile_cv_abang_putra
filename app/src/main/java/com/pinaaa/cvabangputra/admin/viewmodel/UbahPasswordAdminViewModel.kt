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

class UbahPasswordAdminViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _updatePasswordStatus = MutableLiveData<Boolean>()
    val updatePasswordStatus: LiveData<Boolean> = _updatePasswordStatus

    fun updatePassword(idUser: Int, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().updatePassword(idUser, password)
        client.enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(
                call: Call<FeedbackResponse>,
                response: Response<FeedbackResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _updatePasswordStatus.value = true
                    Log.d("UbahPasswordAdminViewModel", "password update successfully")
                } else {
                    _updatePasswordStatus.value = false
                    Log.e("UbahPasswordAdminViewModel", "update failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                _isLoading.value = false
                _updatePasswordStatus.value = false
                Log.e("UbahPasswordAdminViewModel", "update failure: ${t.message}", t)
            }
        })
    }
}