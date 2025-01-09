package com.pinaaa.cvabangputra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pinaaa.cvabangputra.data.local.BarangDao
import com.pinaaa.cvabangputra.data.local.DatabaseRepository
import com.pinaaa.cvabangputra.reseller.viewmodel.FavoriteResellerViewModel
import kotlinx.coroutines.CoroutineScope

class ViewModelFactoryDao (private val dao: BarangDao, private val coroutineScope: CoroutineScope, private val databaseRepository: DatabaseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteResellerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteResellerViewModel(dao, coroutineScope, databaseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}