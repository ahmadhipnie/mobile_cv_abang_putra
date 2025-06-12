package com.pinaaa.cvabangputra.reseller.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataKategoriItem
import com.pinaaa.cvabangputra.databinding.KategoriItemBinding
import com.pinaaa.cvabangputra.reseller.ui.PencarianBarangActivityReseller

class KategoriResellerAdapter: ListAdapter<DataKategoriItem, KategoriResellerAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val apiConfig = ApiConfig()  // Membuat instance ApiConfig untuk mengakses URL base

    @GlideModule
    inner class ViewHolder(private val binding: KategoriItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivKategori: ImageView = binding.ivLogoKategori
        val namaKategori: TextView = binding.tvNamaKategori
        fun bind(data: DataKategoriItem ) {
            Glide.with(itemView.context)
                .load(apiConfig.URL+data.imageUrl)
                .centerCrop()
                .into(ivKategori)
            namaKategori.text = data.namaKategori

            itemView.setOnClickListener {
                Intent(itemView.context, PencarianBarangActivityReseller::class.java).apply {
                    putExtra("idKategori", data.idKategori)
                    putExtra("namaKategori", data.namaKategori)
                    putExtra("jumlahBarang", data.jumlahBarang)
                    itemView.context.startActivity(this)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = KategoriItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataKategoriItem>() {
            override fun areItemsTheSame(oldItem: DataKategoriItem, newItem: DataKategoriItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataKategoriItem,
                newItem: DataKategoriItem
            ): Boolean {
                return oldItem.idKategori == newItem.idKategori
            }
        }
    }

}