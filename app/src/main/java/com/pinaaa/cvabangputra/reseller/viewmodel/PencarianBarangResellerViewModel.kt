package com.pinaaa.cvabangputra.reseller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinaaa.cvabangputra.Repository
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataBarangItem
import kotlinx.coroutines.launch

class PencarianBarangResellerViewModel(private val repository: Repository) : ViewModel() {

    private val _barang = MutableLiveData<List<DataBarangItem>>()
    val barang: LiveData<List<DataBarangItem>> get() = _barang

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getBarangByKategori(kategoriId: Int) {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                val result = repository.getBarangByKategori(kategoriId)
                _barang.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An unexpected error occurred")
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun getBarangsBySearch(namaBarang: String, kategoriId: Int) {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                val result = repository.getBarangsBySearch(namaBarang, kategoriId)
                _barang.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An unexpected error occurred")
            } finally {
                _loading.postValue(false)
            }
        }
    }
}

