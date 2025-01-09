package com.pinaaa.cvabangputra.admin.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataItem
import com.pinaaa.cvabangputra.databinding.KategoriItemBinding
import com.pinaaa.cvabangputra.reseller.adapter.KategoriResellerAdapter
import com.pinaaa.cvabangputra.reseller.ui.PencarianBarangActivityReseller

class KategoriAdminAdapter : ListAdapter<DataItem, KategoriAdminAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val apiConfig = ApiConfig()  // Membuat instance ApiConfig untuk mengakses URL base

    @GlideModule
    inner class ViewHolder(private val binding: KategoriItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivKategori: ImageView = binding.ivLogoKategori
        val namaKategori: TextView = binding.tvNamaKategori
        fun bind(data: DataItem) {
            Glide.with(itemView.context)
                .load(apiConfig.URL+data.imageUrl)
                .centerCrop()
                .into(ivKategori)
            namaKategori.text = data.namaKategori
            namaKategori.setTextColor(Color.parseColor("#000000"))

            itemView.setOnClickListener {
//                Intent(itemView.context, PencarianBarangActivityAdmin::class.java).apply {
//                    putExtra("idKategori", data.idKategori)
//                    putExtra("namaKategori", data.namaKategori)
//                    putExtra("jumlahBarang", data.jumlahBarang)
//                    itemView.context.startActivity(this)
//                }

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