package com.pinaaa.cvabangputra.data.remote.response.reseller

import com.google.gson.annotations.SerializedName

data class RiwayatTransaksiResponse(

	@field:SerializedName("data")
	val data: List<DataTransaksiItem>,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("nama_reseller")
	val namaReseller: String
)

data class User(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("updated_at")
	val updatedAt: Any,

	@field:SerializedName("created_at")
	val createdAt: Any,

	@field:SerializedName("id_user")
	val idUser: Int,

	@field:SerializedName("email")
	val email: String
)

data class DataTransaksiItem(

	@field:SerializedName("barang")
	val barang: Barang,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("jumlah_barang")
	val jumlahBarang: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("total_harga")
	val totalHarga: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("barang_id")
	val barangId: Int,

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("jenis_pengiriman")
	val jenisPengiriman: String,

	@field:SerializedName("status")
	val status: String
)

data class Barang(

	@field:SerializedName("kategori_id")
	val kategoriId: Int,

	@field:SerializedName("updated_at")
	val updatedAt: Any,

	@field:SerializedName("id_barang")
	val idBarang: Int,

	@field:SerializedName("satuan")
	val satuan: String,

	@field:SerializedName("created_at")
	val createdAt: Any,

	@field:SerializedName("nama_barang")
	val namaBarang: String,

	@field:SerializedName("harga_barang")
	val hargaBarang: Int,

	@field:SerializedName("deskripsi_barang")
	val deskripsiBarang: String,

	@field:SerializedName("stok_barang")
	val stokBarang: Int,

	@field:SerializedName("nama_reseller")
	val namaReseller: String,

	@field:SerializedName("alamat_pengiriman")
	val alamatPengiriman: String
)
