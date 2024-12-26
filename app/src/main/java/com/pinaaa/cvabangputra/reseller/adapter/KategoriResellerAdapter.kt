package com.pinaaa.cvabangputra.reseller.adapter

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
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataItem
import com.pinaaa.cvabangputra.databinding.KategoriItemBinding

class KategoriResellerAdapter: ListAdapter<DataItem, KategoriResellerAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val apiConfig = ApiConfig()  // Membuat instance ApiConfig untuk mengakses URL base

    @GlideModule
    inner class ViewHolder(private val binding: KategoriItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivKategori: ImageView = binding.ivLogoKategori
        val namaKategori: TextView = binding.tvNamaKategori
        fun bind(data: DataItem ) {
            Glide.with(itemView.context)
                .load(apiConfig.URL+data.imageUrl)
                .into(ivKategori)
            namaKategori.text = data.namaKategori

            itemView.setOnClickListener {

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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItem,
                newItem: DataItem
            ): Boolean {
                return oldItem.idKategori == newItem.idKategori
            }
        }
    }

}