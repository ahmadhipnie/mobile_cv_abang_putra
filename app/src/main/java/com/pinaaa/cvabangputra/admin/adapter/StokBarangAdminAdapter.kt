package com.pinaaa.cvabangputra.admin.adapter

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.annotation.GlideModule
import com.pinaaa.cvabangputra.admin.viewmodel.StokBarangViewModel
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataBarangItem
import com.pinaaa.cvabangputra.databinding.StokItemBinding
import kotlinx.coroutines.launch

class StokBarangAdminAdapter(private val stokBarangViewModel: StokBarangViewModel)  : ListAdapter<DataBarangItem, StokBarangAdminAdapter.ViewHolder>(DIFF_CALLBACK) {

    val apiConfig = ApiConfig()  // Membuat instance ApiConfig untuk mengakses URL base


    @GlideModule
    inner class ViewHolder(private val binding: StokItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(data: DataBarangItem) {
            // Bind data to view
            binding.etItemNamaBarangAdmin.setText(data.namaBarang)
            binding.tvItemNamaBarangAdmin.text = data.namaBarang
            binding.etItemStokBarangAdmin.setText(data.stokBarang.toString())
            binding.etItemNamaBarangAdmin.isEnabled = false


            binding.etItemStokBarangAdmin.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    val stokBarang = binding.etItemStokBarangAdmin.text.toString().toInt()
                    stokBarangViewModel.updateStokBarang(data.idBarang!!, stokBarang)
                }
            }
//            stokBarangViewModel.viewModelScope.launch {
//                binding.etItemStokBarangAdmin.setOnFocusChangeListener { view, hasFocus ->
//                    if (!hasFocus) {
//                        val stokBarang = binding.etItemStokBarangAdmin.text.toString().toInt()
//                        stokBarangViewModel.updateStokBarang(data.idBarang!!, stokBarang)
//                    }
//                }
//            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StokItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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