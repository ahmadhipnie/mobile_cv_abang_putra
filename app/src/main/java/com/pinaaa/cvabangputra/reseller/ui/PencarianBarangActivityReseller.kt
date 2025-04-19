package com.pinaaa.cvabangputra.reseller.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        // Set title dan jumlah item
        binding.tvTitlePencarianBarangReseller.text = "Kategori $namaKategori"
        binding.tvJumlahItemPencarianBarangReseller.text = "$jumlahBarang Items"

        // Button kembali
        binding.btnBackPencarianBarangReseller.setOnClickListener {
            finish()
        }

        // Setup RecyclerView
        val databaseRepository = DatabaseRepository.getInstance(application)
        val barangAdapter = BarangResellerAdapter(coroutineScope, databaseRepository)
        binding.rvPencarianBarangReseller.apply {
            layoutManager = GridLayoutManager(context, 2)  // Menggunakan 2 kolom
            adapter = barangAdapter
            setHasFixedSize(true)
        }

        // Mengambil data berdasarkan kategori jika kategoriId tidak null
        kategoriId?.let {
            // Pastikan kita mengambil data pertama kali meskipun tidak ada pencarian
            pencarianBarangResellerViewModel.getBarangByKategori(it)
        }

        // Observe perubahan data barang di ViewModel
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

        // Menangani pencarian ketika pengguna menekan tombol enter
        binding.etSearchPencarianBarangReseller.setOnEditorActionListener { _, _, _ ->
            val namaBarang = binding.etSearchPencarianBarangReseller.text.toString()
            if (namaBarang.isNotEmpty()) {
                // Mulai pencarian berdasarkan nama_barang
                pencarianBarangResellerViewModel.getBarangsBySearch(namaBarang, kategoriId!!)
            }
            true
        }

        // Setup pencarian berbasis text change
        setupSearchFeature()
    }

    private fun setupSearchFeature() {
        binding.etSearchPencarianBarangReseller.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    pencarianBarangResellerViewModel.getBarangsBySearch(query, kategoriId!!)
                } else {
                    // Menangani pencarian kosong
                    pencarianBarangResellerViewModel.getBarangsBySearch("", kategoriId!!)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}

