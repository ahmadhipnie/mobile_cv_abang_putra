package com.pinaaa.cvabangputra.reseller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.BarangResponse
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataBarangItem
import retrofit2.Callback

class PencarianBarangResellerViewModel : ViewModel() {

    private val _barang = MutableLiveData<List<DataBarangItem>>()
    val barang: LiveData<List<DataBarangItem>> get() = _barang

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getBarangByKategori(kategoriId: Int) {
        _loading.value = true

        val client = ApiConfig.getApiService().getBarangByKategori(kategoriId)
        client.enqueue(object : Callback<BarangResponse> {
            override fun onResponse(
                call: retrofit2.Call<BarangResponse>,
                response: retrofit2.Response<BarangResponse>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    val barangResponse = response.body()
                    barangResponse?.let {
                        _barang.value = it.dataBarang
                    }
                } else {
                    _error.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: retrofit2.Call<BarangResponse>, t: Throwable) {
                _loading.value = false
                _error.value = "Failure: ${t.message}"
            }
        })
    }

    fun getBarangsBySearch(namaBarang: String, kategoriId: Int) {
        _loading.value = true

        val client = ApiConfig.getApiService().getBarangsBySearch(namaBarang, kategoriId)
        client.enqueue(object : retrofit2.Callback<BarangResponse> {
            override fun onResponse(
                call: retrofit2.Call<BarangResponse>,
                response: retrofit2.Response<BarangResponse>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    val barangResponse = response.body()
                    barangResponse?.let {
                        _barang.value = it.dataBarang
                    }
                } else {
                    _error.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: retrofit2.Call<BarangResponse>, t: Throwable) {
                _loading.value = false
                _error.value = "Failure: ${t.message}"
            }
        })
    }


}

