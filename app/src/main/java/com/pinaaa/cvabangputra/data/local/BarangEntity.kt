package com.pinaaa.cvabangputra.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barang_table")
data class BarangEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_barang")
    var idBarang: Int = 0,

    @ColumnInfo(name = "nama_barang")
    var namaBarang: String,

    @ColumnInfo(name = "harga_barang")
    var hargaBarang: Int,

    @ColumnInfo(name = "stok_barang")
    var stokBarang: Int,

    @ColumnInfo(name = "deskripsi_barang")
    var deskripsiBarang: String,

    @ColumnInfo(name = "satuan")
    var satuan: String,

    @ColumnInfo(name = "kategori")
    var kategori: String,

    @ColumnInfo(name = "image_url")
    var imageUrl: String

)