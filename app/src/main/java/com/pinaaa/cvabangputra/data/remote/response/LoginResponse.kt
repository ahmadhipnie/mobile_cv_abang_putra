package com.pinaaa.cvabangputra.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id_user")
	val idUser: Int? = null,

	@field:SerializedName("id_reseller")
	val idReseller: Int? = null,

	@field:SerializedName("reseller_created_at")
	val resellerCreatedAt: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("nomor_telepon")
	val nomorTelepon: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("tanggal_lahir")
	val tanggalLahir: String? = null,

	@field:SerializedName("foto_profil")
	val fotoProfil: String? = null,

	@field:SerializedName("reseller_updated_at")
	val resellerUpdatedAt: String? = null
)
