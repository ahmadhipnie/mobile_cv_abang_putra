package com.pinaaa.cvabangputra.data.remote.response.reseller

import com.google.gson.annotations.SerializedName

data class FeedbackResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("status")
	val status: String
)
