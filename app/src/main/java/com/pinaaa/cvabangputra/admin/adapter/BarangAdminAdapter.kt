package com.pinaaa.cvabangputra.admin.adapter

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.pinaaa.cvabangputra.admin.ui.DetailBarangActivityAdmin
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataBarangItem
import com.pinaaa.cvabangputra.databinding.BarangItemBinding
import com.pinaaa.cvabangputra.di.injection

class BarangAdminAdapter () : ListAdapter<DataBarangItem, BarangAdminAdapter.ViewHolder>(DIFF_CALLBACK) {

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
            binding.btnFavorite.isGone = true
            binding.btnFavorite.isEnabled = false

            itemView.setOnClickListener() {
                Log.d(TAG, "Item clicked: ${data.idBarang}")
                val intent = Intent(itemView.context, DetailBarangActivityAdmin::class.java)
                intent.putExtra("idBarang", data.idBarang)
                intent.putExtra("namaBarang", data.namaBarang)
                intent.putExtra("hargaBarang", data.hargaBarang)
                intent.putExtra("deskripsiBarang", data.deskripsiBarang)
                intent.putExtra("gambarUrl", data.gambarUrl)
                intent.putExtra("satuanBarang", data.satuan)
                intent.putExtra("stokBarang", data.stokBarang)
                intent.putExtra("namaKategori", data.namaKategori)
                itemView.context.startActivity(intent)
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