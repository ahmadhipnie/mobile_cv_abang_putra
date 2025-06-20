package com.pinaaa.cvabangputra.reseller.adapter


import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataTransaksiItem
import com.pinaaa.cvabangputra.data.remote.response.reseller.FeedbackResponse
import com.pinaaa.cvabangputra.databinding.TransaksiItemBinding
import com.pinaaa.cvabangputra.di.injection.rupiahFormat
import com.pinaaa.cvabangputra.reseller.ui.DetailBarangActivityReseller
import com.pinaaa.cvabangputra.reseller.ui.RiwayatActivityReseller
import retrofit2.Call
import retrofit2.Callback

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
                binding.btnUbahStatusTransaksi.setOnClickListener {
                    // Handle button click to update status
                    apiConfig.getApiService().updateStatusTransaksi(data.id, "selesai").enqueue(object : Callback<FeedbackResponse> {
                        override fun onResponse(call: Call<FeedbackResponse>, response: retrofit2.Response<FeedbackResponse>) {
                            if (response.isSuccessful) {
                                binding.btnUbahStatusTransaksi.text = "Barang Sudah diterima"
                                binding.btnUbahStatusTransaksi.isEnabled = false

                                Toast.makeText(
                                    itemView.context,
                                    "Status transaksi berhasil diubah",
                                    Toast.LENGTH_SHORT
                                ).show()


//                                val intent = Intent(itemView.context, RiwayatActivityReseller::class.java)
//                                itemView.context.startActivity(intent)
                                // tambahkan finish() jika ingin menutup activity ini
                                (itemView.context as RiwayatActivityReseller).finish()
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



