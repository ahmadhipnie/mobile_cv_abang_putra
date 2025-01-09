package com.pinaaa.cvabangputra.reseller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinaaa.cvabangputra.Repository
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataGambarPromoItem
import kotlinx.coroutines.launch

class DetailPromoResellerViewModel(private val repository: Repository) : ViewModel() {

    private val _gambarPromo = MutableLiveData<List<DataGambarPromoItem>>()
    val gambarPromo: LiveData<List<DataGambarPromoItem>> get() = _gambarPromo

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getImagesPromoByIdPromo(id_barang: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getImagesPromoByIdPromo(id_barang)
                _gambarPromo.postValue(result)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "An unexpected error occurred")
            }
        }
    }


}