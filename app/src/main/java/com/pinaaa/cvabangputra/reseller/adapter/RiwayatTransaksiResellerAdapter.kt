package com.pinaaa.cvabangputra.reseller.adapter


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

class RiwayatTransaksiResellerAdapter : ListAdapter<DataTransaksiItem, RiwayatTransaksiResellerAdapter.ViewHolder>(DIFF_CALLBACK){

    val apiConfig = ApiConfig()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TransaksiItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaksiItem = getItem(position)  // Use getItem() to get the current data from the list
        holder.bind(transaksiItem)
    }

    inner class ViewHolder(private val binding: TransaksiItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataTransaksiItem) {
            binding.tvItemNamaBarang.text = data.barang.namaBarang
            binding.tvItemTotalHargaTransaksi.text = "Harga : "+rupiahFormat(data.totalHarga)
            binding.tvItemStatusTransaksi.text = data.jenisPengiriman + "(${data.status})"
            binding.tvItemJumlahBarangTransaksi.text = "Jumlah Barang : " + data.jumlahBarang.toString()

            if (data.status == "diproses"){
                binding.btnUbahStatusTransaksi.visibility = View.GONE
                binding.btnUbahStatusTransaksi.isEnabled = false
            } else if (data.status == "dikirim"){
                binding.btnUbahStatusTransaksi.text = "Barang Sudah diterima"
                binding.btnUbahStatusTransaksi.isEnabled = true
            } else {
                binding.btnUbahStatusTransaksi.visibility = View.GONE
            }

            itemView.setOnClickListener {
                // Handle click event
            }
        }
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



