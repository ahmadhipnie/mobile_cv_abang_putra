package com.pinaaa.cvabangputra.reseller.viewmodel

import androidx.lifecycle.ViewModel
import com.pinaaa.cvabangputra.Repository
import com.pinaaa.cvabangputra.data.remote.response.reseller.FeedbackResponse

class KirimFeedbackResellerViewModel(private val repository: Repository) : ViewModel() {

    // Mengubah return type dari String menjadi FeedbackResponse
    suspend fun kirimFeedbackReseller(email: String, namaReseller: String, rating: Int, isiFeedback: String): FeedbackResponse {
        return repository.kirimFeedbackReseller(email, namaReseller, rating, isiFeedback)
    }
}