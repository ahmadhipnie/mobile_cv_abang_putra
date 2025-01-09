package com.pinaaa.cvabangputra.reseller.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.pinaaa.cvabangputra.data.local.DatabaseRepository
import com.pinaaa.cvabangputra.data.remote.response.reseller.DataBarangItem
import com.pinaaa.cvabangputra.databinding.FragmentFavoritResellerBinding
import com.pinaaa.cvabangputra.reseller.adapter.BarangFavoritResellerAdapter
import com.pinaaa.cvabangputra.reseller.adapter.BarangResellerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class FavoritFragmentReseller : Fragment() {
    private var _binding: FragmentFavoritResellerBinding? = null
    private val binding get() = _binding!!

    private var coroutineScope: CoroutineScope = MainScope()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritResellerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val databaseRepository = DatabaseRepository.getInstance(requireActivity().application)
        val barangFavoritResellerAdapter = BarangFavoritResellerAdapter(coroutineScope, databaseRepository)



        binding.rvFavoritReseller.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = barangFavoritResellerAdapter
        }
        binding.rvFavoritReseller.adapter = barangFavoritResellerAdapter
        databaseRepository.getAllBarang().observe(viewLifecycleOwner) { barangList ->
            if (barangList != null && barangList.isNotEmpty()) {
                val mappedList = barangList.map {
                    DataBarangItem(
                        idBarang = it.idBarang,
                        namaBarang = it.namaBarang,
                        hargaBarang = it.hargaBarang,
                        stokBarang = it.stokBarang,
                        deskripsiBarang = it.deskripsiBarang,
                        satuan = it.satuan,
                        namaKategori = it.kategori,
                        gambarUrl = it.imageUrl
                    )
                }
                barangFavoritResellerAdapter.submitList(mappedList)
                binding.tvNoFavoritReseller.visibility = View.GONE
            } else {
                Log.d("DatabaseData", "Database kosong.")
                barangFavoritResellerAdapter.submitList(emptyList()) // Kosongkan adapter jika tidak ada data
                binding.tvNoFavoritReseller.visibility = View.VISIBLE
            }
        }

        observeDatabase()
    }



    private fun observeDatabase() {
        val databaseRepository = DatabaseRepository.getInstance(requireActivity().application)
        databaseRepository.getAllBarang().observe(viewLifecycleOwner) { barangList ->
            if (!barangList.isNullOrEmpty()) {
                Log.d("DatabaseData", "Barang di database: $barangList")
            } else {
                Log.d("DatabaseData", "Database kosong.")
            }
        }
    }

}