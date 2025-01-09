package com.pinaaa.cvabangputra.data.remote.response.admin

import com.google.gson.annotations.SerializedName

data class PromoAdminResponse(

	@field:SerializedName("data_promo")
	val dataPromo: List<DataPromoItem>,

	@field:SerializedName("status")
	val status: String
)

data class DataPromoItem(

	@field:SerializedName("nama_promo")
	val namaPromo: String,

	@field:SerializedName("tanggal_periode_awal")
	val tanggalPeriodeAwal: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("gambar_url")
	val gambarUrl: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("tanggal_periode_akhir")
	val tanggalPeriodeAkhir: String,

	@field:SerializedName("id_promo")
	val idPromo: Int,

	@field:SerializedName("deskripsi_promo")
	val deskripsiPromo: String
)
