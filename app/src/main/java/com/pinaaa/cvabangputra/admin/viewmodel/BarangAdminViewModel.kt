package com.pinaaa.cvabangputra.admin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.BarangResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataBarangItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataKategoriItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataTransaksiItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.FeedbackResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.KategoriResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class BarangAdminViewModel : ViewModel() {


    private val _barangList = MutableLiveData<List<DataBarangItem>>()
    val barangList: LiveData<List<DataBarangItem>> = _barangList

    private val _kategoriList = MutableLiveData<List<DataKategoriItem>>()
    val kategoriList: LiveData<List<DataKategoriItem>> = _kategoriList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _deleteBarangStatus = MutableLiveData<Boolean>()
    val deleteBarangStatus: LiveData<Boolean> = _deleteBarangStatus

    private val _feedbackResponse = MutableLiveData<FeedbackResponse>()
    val feedbackResponse: LiveData<FeedbackResponse> = _feedbackResponse


    private val _deleteKategori = MutableLiveData<Boolean>()
    val deleteKategori: LiveData<Boolean> = _deleteKategori

    fun fetchBarangs() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getBarangAdmin()
        client.enqueue(object : Callback<BarangResponse> {
            override fun onResponse(
                call: Call<BarangResponse>,
                response: Response<BarangResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val feedbackResponse = response.body()
                    Log.d("FeedbackViewModel", "Response body: $feedbackResponse")
                    feedbackResponse?.let {
                        _barangList.value = it.dataBarang
                    }
                } else {
                    Log.e("FeedbackViewModel", "Error Response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<BarangResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("FeedbackViewModel", "onFailure: ${t.message}", t)
            }
        })
    }

    fun fetchKategori() {

        val client = ApiConfig.getApiService().getKategoriAdmin()
        client.enqueue(object : Callback<KategoriResponse> {
            override fun onResponse(
                call: Call<KategoriResponse>,
                response: Response<KategoriResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val feedbackResponse = response.body()
                    Log.d("FeedbackViewModel", "Response body: $feedbackResponse")
                    feedbackResponse?.let {
                        _kategoriList.value = (it.listData as List<DataKategoriItem>?)!!
                    }
                } else {
                    Log.e("FeedbackViewModel", "Error Response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<KategoriResponse>, t: Throwable) {
                Log.e("FeedbackViewModel", "onFailure: ${t.message}", t)
            }
        })
    }

    fun searchBarangByName(namaBarang: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getBarangsBySearchAdmin(namaBarang)
        client.enqueue(object : Callback<BarangResponse> {
            override fun onResponse(
                call: Call<BarangResponse>,
                response: Response<BarangResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val barangResponse = response.body()
                    barangResponse?.let {
                        _barangList.value = it.dataBarang
                    }
                } else {
                    Log.e(
                        "BarangAdminViewModel",
                        "Search Error Response: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<BarangResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("BarangAdminViewModel", "Search Failure: ${t.message}", t)
            }
        })
    }

    fun deleteBarang(idBarang: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().deleteBarang(idBarang)
        client.enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(
                call: Call<FeedbackResponse>,
                response: Response<FeedbackResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _deleteBarangStatus.value = true
                    Log.d("BarangAdminViewModel", "Barang deleted successfully")
                } else {
                    _deleteBarangStatus.value = false
                    Log.e(
                        "BarangAdminViewModel",
                        "Delete failed: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                _isLoading.value = false
                _deleteBarangStatus.value = false
                Log.e("BarangAdminViewModel", "Delete failure: ${t.message}", t)
            }
        })
    }

    fun addBarang(
        namaBarang: String, hargaBarang: Int, stokBarang: Int,
        deskripsiBarang: String, satuan: String, kategoriId: Int,
        gambar1: File?, gambar2: File?, gambar3: File?
    ) {
        _isLoading.value = true

        // Prepare RequestBody for other data (non-file)
        val namaBarangRequest = RequestBody.create("text/plain".toMediaTypeOrNull(), namaBarang)
        val hargaBarangRequest =
            RequestBody.create("text/plain".toMediaTypeOrNull(), hargaBarang.toString())
        val stokBarangRequest =
            RequestBody.create("text/plain".toMediaTypeOrNull(), stokBarang.toString())
        val deskripsiBarangRequest =
            RequestBody.create("text/plain".toMediaTypeOrNull(), deskripsiBarang)
        val satuanRequest = RequestBody.create("text/plain".toMediaTypeOrNull(), satuan)
        val kategoriIdRequest =
            RequestBody.create("text/plain".toMediaTypeOrNull(), kategoriId.toString())

        // Prepare MultipartBody.Part for images
        val gambarUrl1Part = gambar1?.let {
            val file = File(it.path!!)
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("gambar_url_1", file.name, requestBody)
        }

        val gambarUrl2Part = gambar2?.let {
            val file = File(it.path!!)
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("gambar_url_2", file.name, requestBody)
        }

        val gambarUrl3Part = gambar3?.let {
            val file = File(it.path!!)
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("gambar_url_3", file.name, requestBody)
        }

        val params = mutableMapOf<String, RequestBody>()
        params["nama_barang"] = namaBarangRequest
        params["harga_barang"] = hargaBarangRequest
        params["stok_barang"] = stokBarangRequest
        params["deskripsi_barang"] = deskripsiBarangRequest
        params["satuan"] = satuanRequest
        params["kategori_id"] = kategoriIdRequest

        // Call API to add Barang
        val client = ApiConfig.getApiService()
            .addBarang(params, gambarUrl1Part, gambarUrl2Part, gambarUrl3Part)
        client.enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(
                call: Call<FeedbackResponse>,
                response: Response<FeedbackResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _feedbackResponse.value = response.body()
                } else {
                    Log.e("BarangAdminViewModel", "Failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("BarangAdminViewModel", "onFailure: ${t.message}", t)
            }
        })
    }


    fun updateBarang(idBarang: Int,
        namaBarang: String, hargaBarang: Int, stokBarang: Int,
        deskripsiBarang: String, satuan: String, kategoriId: Int,
        gambar1: File?, gambar2: File?, gambar3: File?
    ) {
        _isLoading.value = true

        // Prepare RequestBody for other data (non-file)
        val idBarangRequest = RequestBody.create("text/plain".toMediaTypeOrNull(), idBarang.toString())
        val namaBarangRequest = RequestBody.create("text/plain".toMediaTypeOrNull(), namaBarang)
        val hargaBarangRequest =
            RequestBody.create("text/plain".toMediaTypeOrNull(), hargaBarang.toString())
        val stokBarangRequest =
            RequestBody.create("text/plain".toMediaTypeOrNull(), stokBarang.toString())
        val deskripsiBarangRequest =
            RequestBody.create("text/plain".toMediaTypeOrNull(), deskripsiBarang)
        val satuanRequest = RequestBody.create("text/plain".toMediaTypeOrNull(), satuan)
        val kategoriIdRequest =
            RequestBody.create("text/plain".toMediaTypeOrNull(), kategoriId.toString())

        // Prepare MultipartBody.Part for images
        val gambarUrl1Part = gambar1?.let {
            val file = File(it.path!!)
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("gambar_url_1", file.name, requestBody)
        }

        val gambarUrl2Part = gambar2?.let {
            val file = File(it.path!!)
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("gambar_url_2", file.name, requestBody)
        }

        val gambarUrl3Part = gambar3?.let {
            val file = File(it.path!!)
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("gambar_url_3", file.name, requestBody)
        }

        val params = mutableMapOf<String, RequestBody>()
        params["id_barang"] = idBarangRequest
        params["nama_barang"] = namaBarangRequest
        params["harga_barang"] = hargaBarangRequest
        params["stok_barang"] = stokBarangRequest
        params["deskripsi_barang"] = deskripsiBarangRequest
        params["satuan"] = satuanRequest
        params["kategori_id"] = kategoriIdRequest

        // Call API to add Barang
        val client = ApiConfig.getApiService()
            .updateBarang(params, gambarUrl1Part, gambarUrl2Part, gambarUrl3Part)
        client.enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(
                call: Call<FeedbackResponse>,
                response: Response<FeedbackResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _feedbackResponse.value = response.body()
                } else {
                    Log.e("BarangAdminViewModel", "Failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("BarangAdminViewModel", "onFailure: ${t.message}", t)
            }
        })
    }


    fun deleteKategoriWithCheck(idKategori: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().deleteKategoriWithCheck(idKategori)
        client.enqueue(object : Callback<FeedbackResponse> {
            override fun onResponse(
                call: Call<FeedbackResponse>,
                response: Response<FeedbackResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val feedbackResponse = response.body()
                    feedbackResponse?.let {
                        if (it.status == "error") {
                           _isLoading.value = false
                            _deleteKategori.value = false
                            Log.e("BarangAdminViewModel", "Delete failed: ${it.message}")

                        } else if (it.status == "success") {
                            _deleteKategori.value = true

                        } else{
                            _deleteKategori.value = false
                            Log.e("BarangAdminViewModel", "Delete failed: ${it.message}")
                        }
                    }
                } else {
                    _deleteKategori.value = false
                    Log.e("BarangAdminViewModel", "Delete failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("BarangAdminViewModel", "Delete failure: ${t.message}", t)
            }
        })
    }


}