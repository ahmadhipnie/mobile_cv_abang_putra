package com.pinaaa.cvabangputra.data.remote.response.reseller

import com.google.gson.annotations.SerializedName

data class GambarPromoResponse(

	@field:SerializedName("data_gambar_promo")
	val dataGambarPromo: List<DataGambarPromoItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataGambarPromoItem(

	@field:SerializedName("nama_promo")
	val namaPromo: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("gambar_url")
	val gambarUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id_gambar_promo")
	val idGambarPromo: Int? = null,

	@field:SerializedName("promo_id")
	val promoId: Int? = null
)
