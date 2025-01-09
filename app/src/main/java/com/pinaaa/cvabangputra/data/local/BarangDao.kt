package com.pinaaa.cvabangputra.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BarangDao {


    @Query("SELECT * FROM barang_table")
    fun getBarang(): LiveData<List<BarangEntity>>

    @Query("SELECT * FROM barang_table WHERE id_barang = :id")
    fun getBarangById(id: Int): LiveData<BarangEntity?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarang(barangEntity: BarangEntity)

    @Query("DELETE FROM barang_table WHERE id_barang = :idBarang")
    suspend fun deleteBarangById(idBarang: Int)  // Ubah method ini
}
