package com.pinaaa.cvabangputra.admin.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinaaa.cvabangputra.admin.ui.TransaksiAdminActivity
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataTransaksiItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.FeedbackResponse
import com.pinaaa.cvabangputra.databinding.TransaksiItemBinding
import com.pinaaa.cvabangputra.di.injection.rupiahFormat
import retrofit2.Call
import retrofit2.Callback

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

               binding.btnUbahStatusTransaksi.setOnClickListener {
                   // Handle button click to update status
                   apiConfig.getApiService().updateStatusTransaksi(data.id, "dikirim").enqueue(object : Callback<FeedbackResponse> {
                       override fun onResponse(call: Call<FeedbackResponse>, response: retrofit2.Response<FeedbackResponse>) {
                           if (response.isSuccessful) {
                               binding.btnUbahStatusTransaksi.text = "Barang Sudah diterima"
                               binding.btnUbahStatusTransaksi.isEnabled = false

                               Toast.makeText(
                                   itemView.context,
                                   "Status transaksi berhasil diubah",
                                   Toast.LENGTH_SHORT
                               ).show()
                               (itemView.context as TransaksiAdminActivity).finish()

                           } else {
                               Toast.makeText(
                                   itemView.context,
                                   "Gagal mengubah status transaksi",
                                   Toast.LENGTH_SHORT
                               ).show()
                               Log.e(TAG, "onResponse: " + response.message() )
                           }
                       }

                       override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                           Toast.makeText(
                               itemView.context,
                               "Gagal mengubah status transaksi: ${t.message}",
                               Toast.LENGTH_SHORT
                           ).show()
                           Log.e(TAG, "onFailure: " + t.message )
                       }
                   })
               }

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