package com.pinaaa.cvabangputra.data.remote.response.reseller

import com.google.gson.annotations.SerializedName

data class BarangResponse(

	@field:SerializedName("data_barang")
	val dataBarang: List<DataBarangItem>,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataBarangItem(

	@field:SerializedName("kategori_id")
	val kategoriId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("id_barang")
	val idBarang: Int? = null,

	@field:SerializedName("satuan")
	val satuan: String? = null,

	@field:SerializedName("gambar_url")
	val gambarUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("nama_barang")
	val namaBarang: String? = null,

	@field:SerializedName("harga_barang")
	val hargaBarang: Int? = null,

	@field:SerializedName("deskripsi_barang")
	val deskripsiBarang: String? = null,

	@field:SerializedName("stok_barang")
	val stokBarang: Int? = null,

	@field:SerializedName("nama_kategori")
	val namaKategori: String? = null
)
