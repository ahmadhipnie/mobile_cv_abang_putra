package com.pinaaa.cvabangputra.admin.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.pinaaa.cvabangputra.admin.ui.UbahKategoriAdminActivity
import com.pinaaa.cvabangputra.databinding.KategoriItemBinding
import com.pinaaa.cvabangputra.admin.viewmodel.BarangAdminViewModel
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataKategoriItem

class KategoriAdminAdapter(private val context: Context, private val viewModel: BarangAdminViewModel) :
    ListAdapter<DataKategoriItem, KategoriAdminAdapter.ViewHolder>(DIFF_CALLBACK) {

    val apiConfig = ApiConfig()

    inner class ViewHolder(private val binding: KategoriItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivKategori: ImageView = binding.ivLogoKategori
        val namaKategori: TextView = binding.tvNamaKategori

        fun bind(data: DataKategoriItem) {
            Glide.with(itemView.context)
                .load(apiConfig.URL + data.imageUrl)
                .centerCrop()
                .into(ivKategori)
            namaKategori.text = data.namaKategori
            namaKategori.setTextColor(Color.parseColor("#000000"))

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, UbahKategoriAdminActivity::class.java)
                intent.putExtra("idKategori", data.idKategori)
                intent.putExtra("namaKategori", data.namaKategori)
                intent.putExtra("imageUrl", data.imageUrl)
                itemView.context.startActivity(intent)
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

    // Fungsi untuk menampilkan konfirmasi dialog sebelum menghapus kategori
    fun showDeleteConfirmationDialog(data: DataKategoriItem) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Konfirmasi Hapus Kategori")
            .setMessage("Apakah Anda yakin ingin menghapus kategori ini?")
            .setPositiveButton("Hapus") { dialog, _ ->
                viewModel.deleteKategoriWithCheck(data.idKategori!!)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
