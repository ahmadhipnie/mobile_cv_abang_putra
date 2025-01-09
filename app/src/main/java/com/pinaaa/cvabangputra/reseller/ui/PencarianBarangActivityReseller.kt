package com.pinaaa.cvabangputra.reseller.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.data.local.DatabaseRepository
import com.pinaaa.cvabangputra.databinding.ActivityPencarianBarangResellerBinding
import com.pinaaa.cvabangputra.reseller.adapter.BarangResellerAdapter
import com.pinaaa.cvabangputra.reseller.adapter.KategoriResellerAdapter
import com.pinaaa.cvabangputra.reseller.viewmodel.BerandaResellerViewModel
import com.pinaaa.cvabangputra.reseller.viewmodel.PencarianBarangResellerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class PencarianBarangActivityReseller : AppCompatActivity() {

    private lateinit var binding: ActivityPencarianBarangResellerBinding
    private val pencarianBarangResellerViewModel by viewModels<PencarianBarangResellerViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var kategoriId: Int? = null
    private var namaKategori: String? = null
    private var jumlahBarang: Int? = null

    private val coroutineScope: CoroutineScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPencarianBarangResellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kategoriId = intent.getIntExtra("idKategori", 0)
        namaKategori = intent.getStringExtra("namaKategori")
        jumlahBarang = intent.getIntExtra("jumlahBarang", 0)

        binding.tvTitlePencarianBarangReseller.text = "Kategori $namaKategori"
        binding.tvJumlahItemPencarianBarangReseller.text = "$jumlahBarang Items"

        binding.btnBackPencarianBarangReseller.setOnClickListener {
           finish()
        }

        // Mengambil data berdasarkan kategori jika kategoriId tidak null
        kategoriId?.let { pencarianBarangResellerViewModel.getBarangByKategori(it) }

        // Setup RecyclerView
        setupRecyclerViewBarang()

        // Observe perubahan data barang di ViewModel
        observeViewModel()

        // Menangani pencarian ketika pengguna menekan tombol enter
        binding.etSearchPencarianBarangReseller.setOnEditorActionListener { _, _, _ ->
            val namaBarang = binding.etSearchPencarianBarangReseller.text.toString()
            if (namaBarang.isNotEmpty()) {
                // Mulai pencarian berdasarkan nama_barang
                pencarianBarangResellerViewModel.getBarangsBySearch(namaBarang, kategoriId!!)
            }
            true
        }
    }

    private fun setupRecyclerViewBarang() {
        // Setup RecyclerView dengan Adapter BarangResellerAdapter
        val databaseRepository = DatabaseRepository.getInstance(application)
        val barangAdapter = BarangResellerAdapter(coroutineScope,databaseRepository)
        binding.rvPencarianBarangReseller.apply {
            layoutManager = GridLayoutManager(context, 2)  // Menggunakan 2 kolom
            adapter = barangAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        // Mengamati data barang yang diterima dari ViewModel
        pencarianBarangResellerViewModel.barang.observe(this, { barangList ->
            // Update RecyclerView dengan data barang
            (binding.rvPencarianBarangReseller.adapter as BarangResellerAdapter).submitList(barangList)
            // Menyembunyikan progress bar jika data sudah dimuat
            binding.progressBarPencarianBarangReseller.visibility = View.GONE
        })

        // Mengamati error message jika terjadi kesalahan
        pencarianBarangResellerViewModel.error.observe(this, { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            binding.progressBarPencarianBarangReseller.visibility = View.GONE
        })
    }
}
