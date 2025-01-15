package com.pinaaa.cvabangputra.admin.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.admin.viewmodel.PromoAdminViewModel
import com.pinaaa.cvabangputra.databinding.ActivityTambahPromoAdminBinding
import com.pinaaa.cvabangputra.di.injection.reduceFileImage
import com.pinaaa.cvabangputra.di.injection.uriToFile

class TambahPromoActivityAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityTambahPromoAdminBinding

    private val viewModel: PromoAdminViewModel by viewModels()



    private var currentImageUriPromo1: Uri? = null
    private var currentImageUriPromo2: Uri? = null
    private var currentImageUriPromo3: Uri? = null


    private var currentImageIndex: Int = 0

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            when (currentImageIndex) {
                1 -> {
                    currentImageUriPromo1 = it
                    showImage(1)
                }
                2 -> {
                    currentImageUriPromo2 = it
                    showImage(2)
                }
                3 -> {
                    currentImageUriPromo3 = it
                    showImage(3)
                }
            }
        } ?: Log.d("Photo Picker", "No media selected")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTambahPromoAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        showLoading(false)

        // Menangani klik untuk memilih gambar barang
        binding.rlImageGambarPromo1TambahAdmin.setOnClickListener {
            currentImageIndex = 1
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.rlImageGambarPromo2TambahAdmin.setOnClickListener {
            currentImageIndex = 2
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.rlImageGambarPromo3TambahAdmin.setOnClickListener {
            currentImageIndex = 3
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnBackTambahPromoAdmin.setOnClickListener {
            finish()
        }

        binding.btnTambahPromoAdmin.setOnClickListener {
            val namaPromo = binding.etNamaPromoTambahAdmin.text.toString()
            val deskripsiPromo = binding.etDeskripsiPromoTambahAdmin.text.toString()
            val tanggalPeriodeAwal = binding.etTanggalPeriodeAwalTambahAdmin.text.toString()
            val tanggalPeriodeAkhir = binding.etTanggalPeriodeAkhirTambahAdmin.text.toString()

            if (namaPromo.isEmpty() || deskripsiPromo.isEmpty() || tanggalPeriodeAwal.isEmpty() || tanggalPeriodeAkhir.isEmpty()) {
                showToast("Semua kolom harus diisi")
            } else {
                showLoading(true)


                // Mengonversi Uri menjadi file dan kompres gambar
                val gambar1File = currentImageUriPromo1?.let { uri -> uriToFile(uri, this).reduceFileImage() }
                val gambar2File = currentImageUriPromo2?.let { uri -> uriToFile(uri, this).reduceFileImage() }
                val gambar3File = currentImageUriPromo3?.let { uri -> uriToFile(uri, this).reduceFileImage() }

                // Menambahkan barang melalui ViewModel
                viewModel.addPromo(
                    namaPromo = namaPromo,
                    deskripsiPromo = deskripsiPromo,
                    tanggalPeriodeAwal = tanggalPeriodeAwal,
                    tanggalPeriodeAkhir = tanggalPeriodeAkhir,
                    gambar1 = gambar1File,
                    gambar2 = gambar2File,
                    gambar3 = gambar3File
                )

                // Observasi respons dari ViewModel
                viewModel.feedbackResponse.observe(this) { feedback ->
                    showLoading(false)
                    if (feedback != null) {
                        showToast("Promo berhasil ditambahkan!")
                        finish() // Kembali ke halaman sebelumnya
                    } else {
                        showToast("Gagal menambahkan promo.")
                    }
                }

                viewModel.isLoading.observe(this) { isLoading ->
                    showLoading(isLoading)
                }
            }
        }
    }


    // Fungsi untuk menampilkan gambar yang dipilih
    private fun showImage(imageIndex: Int) {
        when (imageIndex) {
            1 -> binding.ivImageGambarPromo1TambahAdmin.setImageURI(currentImageUriPromo1)
            2 -> binding.ivImageGambarPromo2TambahAdmin.setImageURI(currentImageUriPromo2)
            3 -> binding.ivImageGambarPromo3TambahAdmin.setImageURI(currentImageUriPromo3)
        }
    }

    // Fungsi untuk menampilkan loading spinner
    private fun showLoading(isLoading: Boolean) {
        binding.progressBarTambahPromoAdmin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // Fungsi untuk menampilkan toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}