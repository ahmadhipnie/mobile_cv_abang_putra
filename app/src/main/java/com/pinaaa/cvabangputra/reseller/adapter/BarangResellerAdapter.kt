package com.pinaaa.cvabangputra.reseller.adapter

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.data.local.BarangEntity
import com.pinaaa.cvabangputra.data.local.DatabaseRepository
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataBarangItem
import com.pinaaa.cvabangputra.databinding.BarangItemBinding
import com.pinaaa.cvabangputra.di.injection
import com.pinaaa.cvabangputra.reseller.ui.DetailBarangActivityReseller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class BarangResellerAdapter(private val coroutineScope: CoroutineScope, private val databaseRepository: DatabaseRepository) : ListAdapter<DataBarangItem, BarangResellerAdapter.ViewHolder>(DIFF_CALLBACK) {

    val apiConfig = ApiConfig()  // Membuat instance ApiConfig untuk mengakses URL base


    @GlideModule
    inner class ViewHolder(private val binding: BarangItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: DataBarangItem) {
            // Bind data to view
            binding.tvNamaBarang.text = data.namaBarang
            binding.tvHargaBarang.text = data.hargaBarang?.let { injection.rupiahFormat(it) }
            // Lakukan binding untuk gambar menggunakan Glide, misalnya
            Glide.with(itemView.context)
                .load(apiConfig.URL + data.gambarUrl)
                .centerCrop()
                .into(binding.imgBarang)




            val barangEntity = BarangEntity(
                data.idBarang!!,
                data.namaBarang!!,
                data.hargaBarang!!,
                data.stokBarang!!,
                data.deskripsiBarang!!,
                data.satuan!!,
                data.namaKategori!!,
                data.gambarUrl!!
            )
            // Observasi apakah barang ada di database
            coroutineScope.launch {
                databaseRepository.isFavorite(data.idBarang!!).observeForever { barang ->
                    if (barang != null) {
                        binding.btnFavorite.setImageResource(R.drawable.ic_favorite_true)
                    } else {
                        binding.btnFavorite.setImageResource(R.drawable.ic_favorite_false)
                    }
                }
            }

            // Tombol favorite
            binding.btnFavorite.setOnClickListener {
                coroutineScope.launch {
                    try {
                        val barang = databaseRepository.isFavorite(data.idBarang!!).value
                        if (barang == null) {
                            // Barang belum ada, tambahkan
                            databaseRepository.insertBarang(barangEntity)
                            binding.btnFavorite.setImageResource(R.drawable.ic_favorite_true)
                            Log.d(TAG, "bind: ${data.idBarang} added")
                        } else {
                            // Barang ada, hapus berdasarkan idBarang
                            databaseRepository.deleteBarangById(data.idBarang!!)
                            binding.btnFavorite.setImageResource(R.drawable.ic_favorite_false)
                            Log.d(TAG, "bind: ${data.idBarang} deleted")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error updating favorite: ${e.message}")
                    }
                }
            }



            itemView.setOnClickListener() {
                Intent(itemView.context, DetailBarangActivityReseller::class.java).apply {
                    putExtra("idBarang", data.idBarang)
                    putExtra("namaBarang", data.namaBarang)
                    putExtra("hargaBarang", data.hargaBarang)
                    putExtra("deskripsiBarang", data.deskripsiBarang)
                    putExtra("gambarUrl", data.gambarUrl)
                    putExtra("satuanBarang", data.satuan)
                    putExtra("stokBarang", data.stokBarang)
                    putExtra("namaKategori", data.namaKategori)
                    itemView.context.startActivity(this)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BarangItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataBarangItem>() {
            override fun areItemsTheSame(
                oldItem: DataBarangItem,
                newItem: DataBarangItem
            ): Boolean {
                return oldItem.idBarang == newItem.idBarang
            }

            override fun areContentsTheSame(
                oldItem: DataBarangItem,
                newItem: DataBarangItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}