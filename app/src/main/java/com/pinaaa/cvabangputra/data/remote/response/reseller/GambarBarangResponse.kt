package com.pinaaa.cvabangputra.data.remote.response.reseller

import com.google.gson.annotations.SerializedName

data class GambarBarangResponse(

	@field:SerializedName("data_gambar_barang")
	val dataGambarBarang: List<DataGambarBarangItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataGambarBarangItem(

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("id_gambar_barang")
	val idGambarBarang: Int? = null,

	@field:SerializedName("gambar_url")
	val gambarUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("barang_id")
	val barangId: Int? = null
)
