package com.pinaaa.cvabangputra.data.remote.response.reseller

import com.google.gson.annotations.SerializedName

data class KategoriResponse(

    @field:SerializedName("data")
	val listData: List<DataKategoriItem>,

    @field:SerializedName("status")
	val status: String
)

data class DataKategoriItem(

	@field:SerializedName("id_kategori")
	val idKategori: Int,

	@field:SerializedName("updated_at")
	val updatedAt: Any,

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("created_at")
	val createdAt: Any,

	@field:SerializedName("nama_kategori")
	val namaKategori: String,

	@field:SerializedName("jumlah_barang")
	val jumlahBarang: Int
)
