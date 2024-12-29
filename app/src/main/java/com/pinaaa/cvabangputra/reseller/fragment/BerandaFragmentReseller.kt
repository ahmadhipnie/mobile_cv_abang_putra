package com.pinaaa.cvabangputra.reseller.fragment

import android.content.ContentValues.TAG
import android.content.Intent
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
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.databinding.FragmentBerandaResellerBinding
import com.pinaaa.cvabangputra.reseller.adapter.BarangResellerAdapter
import com.pinaaa.cvabangputra.reseller.adapter.KategoriResellerAdapter
import com.pinaaa.cvabangputra.reseller.ui.DetailPromoActivityReseller
import com.pinaaa.cvabangputra.reseller.viewmodel.BerandaResellerViewModel

class BerandaFragmentReseller : Fragment() {

    private var _binding: FragmentBerandaResellerBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private val apiConfig = ApiConfig()  // Membuat instance ApiConfig untuk mengakses URL base


    private val berandaResellerViewModel by viewModels<BerandaResellerViewModel> {
        ViewModelFactory.getInstance(requireActivity())


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBerandaResellerBinding.inflate(inflater, container, false)

        // Menampilkan nama user dari sharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("USER", MODE_PRIVATE)
        binding.tvNamaUserBerandaReseller.text = sharedPreferences.getString("nama", "")

        // Menyiapkan RecyclerView untuk kategori dan barang
        setupRecyclerView()
        setupRecyclerViewBarang()

        // Menampilkan loading sebelum data selesai
        showLoading(true)

        // Memanggil API untuk kategori dan barang
        berandaResellerViewModel.getKategori()
        berandaResellerViewModel.getBarang()

        // Mengamati perubahan pada data kategori dan barang
        observeViewModel()

        // Mengamati error jika ada
        observeBarangViewModel()

        berandaResellerViewModel.getAllGambarPromo()
        berandaResellerViewModel.gambarPromo.observe(requireActivity()) { images ->
            if (!images.isNullOrEmpty()) {
                val imageList = images.mapNotNull { item ->
                    item.gambarUrl?.let {
                        val fullImageUrl = apiConfig.URL + it
                        SlideModel(fullImageUrl, ScaleTypes.CENTER_CROP)
                    }

                }
                binding.imageSliderBerandaReseller.setImageList(imageList)

                binding.imageSliderBerandaReseller.setItemClickListener(object : ItemClickListener {
                    override fun doubleClick(position: Int) {

                    }

                    override fun onItemSelected(position: Int) {
                        val intent = Intent(requireActivity(), DetailPromoActivityReseller::class.java)
                        intent.putExtra("gambar_url", images[position].gambarUrl)
                        intent.putExtra("id_gambar_promo", images[position].idGambarPromo)
                        intent.putExtra("promo_id", images[position].promoId)
                        startActivity(intent)
                    }
                })
            } else {
                Toast.makeText(requireActivity(), "Tidak ada gambar untuk barang ini.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        val kategoriResellerAdapter = KategoriResellerAdapter()
        binding.rvKategoriBerandaReseller.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = kategoriResellerAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViewBarang() {
        val barangAdapter = BarangResellerAdapter()
        binding.rvPalingBanyakDibeliBerandaReseller.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = barangAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        berandaResellerViewModel.kategori.observe(viewLifecycleOwner) { kategoriList ->
            (binding.rvKategoriBerandaReseller.adapter as KategoriResellerAdapter).submitList(kategoriList)
            checkDataLoaded()  // Cek apakah data sudah lengkap untuk ditampilkan
        }

        berandaResellerViewModel.barang.observe(viewLifecycleOwner) { barangList ->
            if (barangList != null && barangList.isNotEmpty()) {
                (binding.rvPalingBanyakDibeliBerandaReseller.adapter as BarangResellerAdapter).submitList(barangList)
            } else {
                Toast.makeText(context, "Data barang kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeBarangViewModel() {
        berandaResellerViewModel.barang.observe(viewLifecycleOwner) { barangList ->
            if (barangList != null && barangList.isNotEmpty()) {
                (binding.rvPalingBanyakDibeliBerandaReseller.adapter as BarangResellerAdapter).submitList(barangList)
            } else {
                Toast.makeText(context, "Data barang kosong", Toast.LENGTH_SHORT).show()
            }
            checkDataLoaded()  // Cek apakah data sudah lengkap untuk ditampilkan
        }

        berandaResellerViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                Log.e(TAG, "observeBarangViewModel: $errorMessage")
            } else {
                Toast.makeText(context, "Error loading barang", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        // Menampilkan atau menyembunyikan loading indicator
        binding.progressBarBerandaReseller.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun checkDataLoaded() {
        // Cek apakah kategori dan barang sudah ter-load dan tampilkan RecyclerView jika sudah lengkap
        if (berandaResellerViewModel.kategori.value != null && berandaResellerViewModel.barang.value != null) {
            showLoading(false)  // Sembunyikan loading setelah data selesai dimuat
        }
    }
}
