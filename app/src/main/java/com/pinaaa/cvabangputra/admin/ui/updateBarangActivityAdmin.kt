package com.pinaaa.cvabangputra.admin.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.admin.viewmodel.BarangAdminViewModel
import com.pinaaa.cvabangputra.databinding.ActivityUpdateBarangAdminBinding
import com.pinaaa.cvabangputra.di.injection.reduceFileImage
import com.pinaaa.cvabangputra.di.injection.uriToFile

class updateBarangActivityAdmin : AppCompatActivity() {
    private lateinit var binding : ActivityUpdateBarangAdminBinding

    private var currentImageUriBarang1: Uri? = null
    private var currentImageUriBarang2: Uri? = null
    private var currentImageUriBarang3: Uri? = null

    private val viewModel: BarangAdminViewModel by viewModels()

    private var currentImageIndex: Int = 0

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            when (currentImageIndex) {
                1 -> {
                    currentImageUriBarang1 = it
                    showImage(1)
                }
                2 -> {
                    currentImageUriBarang2 = it
                    showImage(2)
                }
                3 -> {
                    currentImageUriBarang3 = it
                    showImage(3)
                }
            }
        } ?: Log.d("Photo Picker", "No media selected")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateBarangAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idBarang = intent.getIntExtra("idBarang", 0)
        val namaBarang = intent.getStringExtra("namaBarang")
        val hargaBarang = intent.getIntExtra("hargaBarang", 0)
        val deskripsiBarang = intent.getStringExtra("deskripsiBarang")
        val gambarUrl = intent.getStringExtra("gambarUrl")
        val satuanBarang = intent.getStringExtra("satuanBarang")
        val stokBarang = intent.getIntExtra("stokBarang", 0)
        val namaKategori = intent.getStringExtra("namaKategori")

        with(binding){
            etNamaBarangUpdateAdmin.setText(namaBarang)
            etHargaBarangUpdateAdmin.setText(hargaBarang.toString())
            etStokBarangUpdateAdmin.setText(stokBarang.toString())
            etDeskripsiBarangUpdateAdmin.setText(deskripsiBarang)

        }


        showLoading(false)

        // Menangani klik untuk memilih gambar barang
        binding.rlImageGambarBarang1UpdateAdmin.setOnClickListener {
            currentImageIndex = 1
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.rlImageGambarBarang2UpdateAdmin.setOnClickListener {
            currentImageIndex = 2
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.rlImageGambarBarang3UpdateAdmin.setOnClickListener {
            currentImageIndex = 3
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnBackUpdateBarangAdmin.setOnClickListener {
            finish()
        }

        // Setup Spinner satuan
        val listSatuan = listOf("Pax", "Bungkus", "Lusin", "Kodi", "Pcs", "Box")
        binding.spinnerSatuanBarangUpdateAdmin.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listSatuan
        )

        viewModel.fetchKategori()
        // Observasi kategori dari ViewModel
        viewModel.kategoriList.observe(this) { kategoriList ->
            if (!kategoriList.isNullOrEmpty()) {
                val kategoriNames = kategoriList.map { it.namaKategori ?: "Tanpa Nama" }
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    kategoriNames
                )
                binding.spinnerKategoriBarangUpdateAdmin.adapter = adapter
            }
        }

        // Button untuk menambahkan barang
        binding.btnUpdateBarangAdmin.setOnClickListener {
            val namaBarang = binding.etNamaBarangUpdateAdmin.text.toString()
            val hargaBarang = binding.etHargaBarangUpdateAdmin.text.toString().toIntOrNull()
            val stokBarang = binding.etStokBarangUpdateAdmin.text.toString().toIntOrNull()
            val deskripsiBarang = binding.etDeskripsiBarangUpdateAdmin.text.toString()
            val satuan = binding.spinnerSatuanBarangUpdateAdmin.selectedItem.toString()
            val selectedPosition = binding.spinnerKategoriBarangUpdateAdmin.selectedItemPosition
            val kategoriId = viewModel.kategoriList.value?.getOrNull(selectedPosition)?.idKategori

            if (namaBarang.isEmpty() || hargaBarang == null || stokBarang == null || deskripsiBarang.isEmpty()) {
                showToast("Semua field wajib diisi dengan benar!")
                return@setOnClickListener
            }

            showLoading(true)

            // Mengonversi Uri menjadi file dan kompres gambar
            val gambar1File = currentImageUriBarang1?.let { uri -> uriToFile(uri, this).reduceFileImage() }
            val gambar2File = currentImageUriBarang2?.let { uri -> uriToFile(uri, this).reduceFileImage() }
            val gambar3File = currentImageUriBarang3?.let { uri -> uriToFile(uri, this).reduceFileImage() }

            // Menambahkan barang melalui ViewModel
            viewModel.updateBarang(
                idBarang = idBarang,
                namaBarang = namaBarang,
                hargaBarang = hargaBarang,
                stokBarang = stokBarang,
                deskripsiBarang = deskripsiBarang,
                satuan = satuan,
                kategoriId = kategoriId ?: 0,
                gambar1 = gambar1File,
                gambar2 = gambar2File,
                gambar3 = gambar3File
            )

            // Observasi respons dari ViewModel
            viewModel.feedbackResponse.observe(this) { feedback ->
                showLoading(false)
                if (feedback != null) {
                    showToast("Barang berhasil ditambahkan!, kembali ke halaman beranda untuk merefresh data")
                    finish() // Kembali ke halaman sebelumnya

                } else {
                    showToast("Gagal menambahkan barang.")
                }
            }

            viewModel.isLoading.observe(this) { isLoading ->
                showLoading(isLoading)
            }
        }
    }

    // Fungsi untuk menampilkan gambar yang dipilih
    private fun showImage(imageIndex: Int) {
        when (imageIndex) {
            1 -> binding.ivImageGambarBarang1UpdateAdmin.setImageURI(currentImageUriBarang1)
            2 -> binding.ivImageGambarBarang2UpdateAdmin.setImageURI(currentImageUriBarang2)
            3 -> binding.ivImageGambarBarang3UpdateAdmin.setImageURI(currentImageUriBarang3)
        }
    }

    // Fungsi untuk menampilkan loading spinner
    private fun showLoading(isLoading: Boolean) {
        binding.progressBarUpdateBarangAdmin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // Fungsi untuk menampilkan toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}