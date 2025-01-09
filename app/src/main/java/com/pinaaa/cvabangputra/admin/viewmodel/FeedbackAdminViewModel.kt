package com.pinaaa.cvabangputra.admin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinaaa.cvabangputra.Repository
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.admin.DataFeedbackItem
import com.pinaaa.cvabangputra.data.remote.response.admin.FeedbackAdminResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackAdminViewModel(private val repository: Repository) : ViewModel() {

    private val _feedbackList = MutableLiveData<List<DataFeedbackItem>>()
    val feedbackList: LiveData<List<DataFeedbackItem>> = _feedbackList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchFeedbacks() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getAllFeedback()
        client.enqueue(object : Callback<FeedbackAdminResponse> {
            override fun onResponse(
                call: Call<FeedbackAdminResponse>,
                response: Response<FeedbackAdminResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val feedbackResponse = response.body()
                    Log.d("FeedbackViewModel", "Response body: $feedbackResponse")
                    feedbackResponse?.let {
                        _feedbackList.value = it.dataFeedback
                    }
                } else {
                    Log.e("FeedbackViewModel", "Error Response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FeedbackAdminResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("FeedbackViewModel", "onFailure: ${t.message}", t)
            }
        })
    }
}
