package com.pinaaa.cvabangputra.reseller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinaaa.cvabangputra.Repository
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataGambarBarangItem
import kotlinx.coroutines.launch

class DetailBarangResellerViewModel(private val repository: Repository) : ViewModel() {

    private val _gambarBarang = MutableLiveData<List<DataGambarBarangItem>>()
    val gambarBarang: LiveData<List<DataGambarBarangItem>> get() = _gambarBarang

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getImagesBarangByIdBarang(id_barang: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getImagesBarangByIdBarang(id_barang)
                _gambarBarang.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An unexpected error occurred")
            }
        }
    }


}