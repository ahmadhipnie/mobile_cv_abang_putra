package com.pinaaa.cvabangputra.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DatabaseRepository private constructor(private val application: Application) {

    private val barangDao: BarangDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = BarangDatabase.getDatabase(application)
        barangDao = db.barangDao()
    }

    suspend fun insertBarang(barangEntity: BarangEntity) {
        barangDao.insertBarang(barangEntity)
    }

    suspend fun deleteBarangById(idBarang: Int) {
        barangDao.deleteBarangById(idBarang)
    }

    fun getAllBarang(): LiveData<List<BarangEntity>> {
        return barangDao.getBarang()
    }



    // Fungsi untuk mengecek apakah barang sudah ada
    fun isFavorite(idBarang: Int): LiveData<BarangEntity?> {
        return barangDao.getBarangById(idBarang)
    }



    companion object {
        @Volatile
        private var instance: DatabaseRepository? = null

        fun getInstance(application: Application): DatabaseRepository =
            instance ?: synchronized(this) {
                instance ?: DatabaseRepository(application).also { instance = it }
            }
    }
}