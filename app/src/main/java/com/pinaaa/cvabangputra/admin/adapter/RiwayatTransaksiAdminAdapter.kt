package com.pinaaa.cvabangputra.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataTransaksiItem
import com.pinaaa.cvabangputra.databinding.TransaksiItemBinding
import com.pinaaa.cvabangputra.di.injection.rupiahFormat

class RiwayatTransaksiAdminAdapter : ListAdapter<DataTransaksiItem, RiwayatTransaksiAdminAdapter.ViewHolder>(DIFF_CALLBACK) {

    val apiConfig = ApiConfig()


    inner class ViewHolder(private val binding: TransaksiItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataTransaksiItem) {

            binding.tvItemNamaBarang.text = data.barang.namaBarang
            binding.tvItemTotalHargaTransaksi.text = "Harga : "+rupiahFormat(data.totalHarga)
            binding.tvItemStatusTransaksi.text = data.jenisPengiriman + "(${data.status})"
            binding.tvItemJumlahBarangTransaksi.text = "Jumlah Barang : " + data.jumlahBarang.toString()

            if (data.status == "diproses"){
                binding.btnUbahStatusTransaksi.text = "ubah ke dikirim"
                binding.btnUbahStatusTransaksi.isEnabled = true
                binding.btnUbahStatusTransaksi.visibility = View.VISIBLE
            } else if (data.status == "dikirim"){
                binding.btnUbahStatusTransaksi.visibility = View.GONE
                binding.btnUbahStatusTransaksi.isEnabled = false
            } else {
                binding.btnUbahStatusTransaksi.visibility = View.GONE
                binding.btnUbahStatusTransaksi.isEnabled = false
            }

            itemView.setOnClickListener {
                // Handle click event
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RiwayatTransaksiAdminAdapter.ViewHolder {
        val binding = TransaksiItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RiwayatTransaksiAdminAdapter.ViewHolder,
        position: Int
    ) {
        val transaksiItem = getItem(position)
        holder.bind(transaksiItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataTransaksiItem>() {
            override fun areItemsTheSame(oldItem: DataTransaksiItem, newItem: DataTransaksiItem): Boolean {
                return oldItem.id == newItem.id  // Assuming `id` is unique for each transaction
            }

            override fun areContentsTheSame(oldItem: DataTransaksiItem, newItem: DataTransaksiItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}