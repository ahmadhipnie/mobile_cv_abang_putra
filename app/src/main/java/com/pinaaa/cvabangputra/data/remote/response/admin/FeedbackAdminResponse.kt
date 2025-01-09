package com.pinaaa.cvabangputra.data.remote.response.admin

import com.google.gson.annotations.SerializedName

data class FeedbackAdminResponse(

	@field:SerializedName("data_feedback")
	val dataFeedback: List<DataFeedbackItem>,

	@field:SerializedName("status")
	val status: String
)

data class DataFeedbackItem(

	@field:SerializedName("id_feedback")
	val idFeedback: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("isi_feedback")
	val isiFeedback: String,

	@field:SerializedName("rating")
	val rating: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("nama_reseller")
	val namaReseller: String,

	@field:SerializedName("email")
	val email: String
)
