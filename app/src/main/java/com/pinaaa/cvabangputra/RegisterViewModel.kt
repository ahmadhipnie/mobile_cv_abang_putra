package com.pinaaa.cvabangputra

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataBarangItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.FeedbackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _addUsersStatus = MutableLiveData<Boolean>()
    val addUsersStatus: LiveData<Boolean> = _addUsersStatus

    fun addUsers(email: String, password:String, nama:String, nomorTelepon:String, tanggalLahir:String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().addUserAndReseller(email, password, nama, nomorTelepon, tanggalLahir)
        client.enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(
                call: Call<FeedbackResponse>,
                response: Response<FeedbackResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _addUsersStatus.value = true
                    Log.d("RegisterViewModel", "register successfully")
                } else {
                    _addUsersStatus.value = false
                    Log.e("RegisterViewModel", "register failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                _isLoading.value = false
                _addUsersStatus.value = false
                Log.e("RegisterViewModel", "register failure: ${t.message}", t)
            }
        })
    }


}