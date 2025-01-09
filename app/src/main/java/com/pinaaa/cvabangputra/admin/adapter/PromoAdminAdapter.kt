package com.pinaaa.cvabangputra.admin.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.pinaaa.cvabangputra.admin.ui.PromoActivityAdmin
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.admin.DataPromoItem
import com.pinaaa.cvabangputra.databinding.PromoItemBinding
import com.pinaaa.cvabangputra.di.injection
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PromoAdminAdapter() :
    ListAdapter<DataPromoItem, PromoAdminAdapter.ViewHolder>(DIFF_CALLBACK) {

    val apiConfig = ApiConfig()  // Membuat instance ApiConfig untuk mengakses URL base


    @GlideModule
    inner class ViewHolder(private val binding: PromoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: DataPromoItem) {
            binding.tvItemNamaPromo.text = data.namaPromo
            binding.tvItemTanggalPeriodeAwal.text = "Periode Awal :" + formatTanggal(data.tanggalPeriodeAwal)
            binding.tvItemTanggalPeriodeAkhir.text = "Periode Akhir :" + formatTanggal(data.tanggalPeriodeAkhir)
            // Lakukan binding untuk gambar menggunakan Glide, misalnya
            Glide.with(itemView.context)
                .load(apiConfig.URL + data.gambarUrl)
                .centerCrop()
                .into(binding.imgItemGambarPromo)

            itemView.setOnClickListener() {
                val intent = Intent(itemView.context, PromoActivityAdmin::class.java)
                intent.putExtra("idPromo", data.idPromo)
                intent.putExtra("namaPromo", data.namaPromo)
                intent.putExtra("tanggalPeriodeAwal", data.tanggalPeriodeAwal)
                intent.putExtra("tanggalPeriodeAkhir", data.tanggalPeriodeAkhir)
                intent.putExtra("gambarUrl", data.gambarUrl)
                intent.putExtra("deskripsiPromo", data.deskripsiPromo)
                intent.putExtra("created_at", data.createdAt)
                intent.putExtra("updated_at", data.updatedAt)
                itemView.context.startActivity(intent)
            }
        }

        // Fungsi untuk memformat tanggal
        private fun formatTanggal(dateString: String): String {
            return try {
                val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = inputDateFormat.parse(dateString)
                val outputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
                outputDateFormat.format(date ?: Date())
            } catch (e: Exception) {
                dateString // Jika gagal memformat, gunakan string asli
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PromoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataPromoItem>() {
            override fun areItemsTheSame(
                oldItem: DataPromoItem,
                newItem: DataPromoItem
            ): Boolean {
                return oldItem.idPromo == newItem.idPromo
            }

            override fun areContentsTheSame(
                oldItem: DataPromoItem,
                newItem: DataPromoItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}