package com.pinaaa.cvabangputra.reseller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinaaa.cvabangputra.Repository
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataBarangItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataItem
import kotlinx.coroutines.launch

class BerandaResellerViewModel(private val repository: Repository) : ViewModel() {

    private val _kategori = MutableLiveData<List<DataItem>>()
    val kategori: LiveData<List<DataItem>> get() = _kategori

    private val _barang = MutableLiveData<List<DataBarangItem>>()
    val barang: LiveData<List<DataBarangItem>> get() = _barang

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getKategori() {
        viewModelScope.launch {
            try {
                val result = repository.getKategori()
                _kategori.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An unexpected error occurred")
            }
        }
    }

    fun getBarang() {
        viewModelScope.launch {
            try {
                val result = repository.getBarang()
                _barang.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An unexpected error occurred")
            }
        }
    }
}
