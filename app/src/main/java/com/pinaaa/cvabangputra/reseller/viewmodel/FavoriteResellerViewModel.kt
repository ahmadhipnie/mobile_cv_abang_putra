package com.pinaaa.cvabangputra.reseller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pinaaa.cvabangputra.data.local.BarangDao
import com.pinaaa.cvabangputra.data.local.BarangEntity
import com.pinaaa.cvabangputra.data.local.DatabaseRepository
import kotlinx.coroutines.CoroutineScope

class FavoriteResellerViewModel(dao: BarangDao, private val coroutineScope: CoroutineScope, private val databaseRepository: DatabaseRepository) : ViewModel() {


    suspend fun getFavoriteReseller() : LiveData<List<BarangEntity>> {
        return databaseRepository.getAllBarang()
    }


}