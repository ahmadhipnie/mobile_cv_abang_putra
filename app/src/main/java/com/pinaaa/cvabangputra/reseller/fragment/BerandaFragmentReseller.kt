package com.pinaaa.cvabangputra.reseller.fragment

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.databinding.FragmentBerandaResellerBinding
import com.pinaaa.cvabangputra.reseller.adapter.BarangResellerAdapter
import com.pinaaa.cvabangputra.reseller.adapter.KategoriResellerAdapter
import com.pinaaa.cvabangputra.reseller.viewmodel.BerandaResellerViewModel

class BerandaFragmentReseller : Fragment() {

    private var _binding: FragmentBerandaResellerBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private val berandaResellerViewModel by viewModels<BerandaResellerViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBerandaResellerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("USER", MODE_PRIVATE)

        binding.tvNamaUserBerandaReseller.text = sharedPreferences.getString("nama", "")

        setupRecyclerView()
        setupRecyclerViewBarang()
        observeViewModel()
        observebarangViewModel()
    }

    private fun setupRecyclerViewBarang() {
        val barangAdapter = BarangResellerAdapter()
        binding.rvPalingBanyakDibeliBerandaReseller.apply {
            // Menggunakan GridLayoutManager untuk 2 kolom vertikal
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = barangAdapter
            setHasFixedSize(true)
        }    }

    private fun setupRecyclerView() {
        val kategoriResellerAdapter = KategoriResellerAdapter()
        binding.rvKategoriBerandaReseller.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = kategoriResellerAdapter
            setHasFixedSize(true)
        }


    }

    private fun observeViewModel() {
        berandaResellerViewModel.kategori.observe(viewLifecycleOwner) { kategoriList ->
            (binding.rvKategoriBerandaReseller.adapter as KategoriResellerAdapter).submitList(kategoriList)
        }

        berandaResellerViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                Log.e(TAG, "observeViewModel: ", Throwable(errorMessage))
            } else {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
        }

        berandaResellerViewModel.getKategori()
    }

    private fun observebarangViewModel() {
        berandaResellerViewModel.barang.observe(viewLifecycleOwner) { barangList ->
            if (barangList != null && barangList.isNotEmpty()) {
                (binding.rvPalingBanyakDibeliBerandaReseller.adapter as BarangResellerAdapter).submitList(barangList)
            } else {
                Toast.makeText(context, "Data barang kosong", Toast.LENGTH_SHORT).show()
            }
        }

        berandaResellerViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        berandaResellerViewModel.getBarang()  // Memanggil API untuk mengambil barang
    }


}