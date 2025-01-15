package com.pinaaa.cvabangputra.admin.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.admin.viewmodel.BarangAdminViewModel
import com.pinaaa.cvabangputra.admin.viewmodel.PromoAdminViewModel
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.databinding.ActivityDetailBarangAdminBinding
import com.pinaaa.cvabangputra.di.injection
import com.pinaaa.cvabangputra.reseller.viewmodel.DetailBarangResellerViewModel

class DetailBarangActivityAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBarangAdminBinding

    private val detailBarangResellerViewModel by viewModels<DetailBarangResellerViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val barangAdminViewModel by viewModels<BarangAdminViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val apiConfig = ApiConfig()  // Membuat instance ApiConfig untuk mengakses URL base


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBarangAdminBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBackDetailBarangAdmin.setOnClickListener {
            finish()
        }

        val idBarang = intent.getIntExtra("idBarang", 0)
        val namaBarang = intent.getStringExtra("namaBarang")
        val hargaBarang = intent.getIntExtra("hargaBarang", 0)
        val deskripsiBarang = intent.getStringExtra("deskripsiBarang")
        val gambarUrl = intent.getStringExtra("gambarUrl")
        val satuanBarang = intent.getStringExtra("satuanBarang")
        val stokBarang = intent.getIntExtra("stokBarang", 0)
        val namaKategori = intent.getStringExtra("namaKategori")

        binding.tvNamaBarangDetailBarangAdmin.text = "${namaBarang} - ${stokBarang} ${satuanBarang}"
        binding.tvDeskripsiBarangDetailBarangAdmin.text = deskripsiBarang
        binding.tvHargaBarangDetailBarangAdmin.text = injection.rupiahFormat(hargaBarang)
        binding.tvNamaKategoriDetailBarangAdmin.text = namaKategori

        binding.btnHapusDetailBarangAdmin.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Barang")
                .setMessage("Apakah Anda yakin ingin menghapus barang ini?")
                .setPositiveButton("Ya") { _, _ ->
                    barangAdminViewModel.deleteBarang(idBarang)
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        barangAdminViewModel.deleteBarangStatus.observe(this) { isDeleted ->
            if (isDeleted) {
                Toast.makeText(this, "Barang deleted successfully", Toast.LENGTH_SHORT).show()
                finish() // Kembali ke halaman sebelumnya setelah berhasil dihapus
            } else {
                Toast.makeText(this, "Failed to delete barang", Toast.LENGTH_SHORT).show()
            }
        }

        // Ambil data gambar dari ViewModel
        detailBarangResellerViewModel.getImagesBarangByIdBarang(intent.getIntExtra("idBarang", 0))
        detailBarangResellerViewModel.gambarBarang.observe(this) { images ->
            if (!images.isNullOrEmpty()) {
                val imageList = images.mapNotNull { item ->
                    item.gambarUrl?.let {
                        val fullImageUrl = apiConfig.URL + it
                        SlideModel(fullImageUrl, ScaleTypes.CENTER_CROP)
                    }
                }
                binding.imageSliderDetailBarangAdmin.setImageList(imageList)
            } else {
                Toast.makeText(this, "Tidak ada gambar untuk barang ini.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}