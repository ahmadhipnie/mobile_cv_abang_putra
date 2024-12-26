package com.pinaaa.cvabangputra.reseller.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.databinding.ActivityDetailBarangResellerBinding
import com.pinaaa.cvabangputra.di.injection
import com.pinaaa.cvabangputra.reseller.viewmodel.DetailBarangResellerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

class DetailBarangActivityReseller : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBarangResellerBinding

    private val detailBarangResellerViewModel by viewModels<DetailBarangResellerViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val apiConfig = ApiConfig()  // Membuat instance ApiConfig untuk mengakses URL base

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBarangResellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val namaBarang = intent.getStringExtra("namaBarang") ?: ""
        val gambarUrl = intent.getStringExtra("gambarUrl") ?: ""
        val fullImageUrl = apiConfig.URL + gambarUrl // Gabungkan URL base dengan gambarUrl

        binding.tvNamaKategoriDetailBarangReseller.text = intent.getStringExtra("namaKategori")
        binding.tvNamaBarangDetailBarangReseller.text = "$namaBarang - ${
            intent.getIntExtra(
                "stokBarang",
                0
            )
        } ${intent.getStringExtra("satuanBarang")}"
        binding.tvHargaBarangDetailBarangReseller.text =
            injection.rupiahFormat(intent.getIntExtra("hargaBarang", 0))
        val deskripsiBarang = intent.getStringExtra("deskripsiBarang") ?: ""
        // Menampilkan deskripsi dengan cek panjangnya
        displayDescription(deskripsiBarang)

        binding.btnBackDetailBarangReseller.setOnClickListener { finish() }

        binding.btnShareDetailBarangReseller.setOnClickListener {
            // Jika gambar URL ada, lakukan share gambar bersama teks
            if (gambarUrl.isNotEmpty()) {
                shareProductWithImage(fullImageUrl, namaBarang)
            } else {
                // Jika gambar URL tidak tersedia, hanya bagikan teks
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Cek barang ini di aplikasi CV Abang Putra: $namaBarang"
                    )
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, "Bagikan Produk"))
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
                binding.imageSliderDetailBarangReseller.setImageList(imageList)
            } else {
                Toast.makeText(this, "Tidak ada gambar untuk barang ini.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        detailBarangResellerViewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            Log.e(TAG, "onCreate: $errorMessage")
        }

        binding.btnPesanSekarangDetailBarangReseller.setOnClickListener {
            val namaBarang = intent.getStringExtra("namaBarang") ?: ""
            val gambarUrl = intent.getStringExtra("gambarUrl") ?: ""
            val fullImageUrl = apiConfig.URL + gambarUrl // Gabungkan URL base dengan gambarUrl

            // Kirim pesan melalui WhatsApp
            sendMessageToWhatsapp(fullImageUrl, namaBarang)
        }

    }

    private fun sendMessageToWhatsapp(imageUrl: String, productName: String) {
        try {
            // Jalankan pengunduhan gambar di thread background
            GlobalScope.launch(Dispatchers.IO) {
                val imageUri = downloadImageToCache(imageUrl) // Gunakan fungsi downloadImageToCache
                if (imageUri != null) {
                    // Kirim hasilnya kembali ke thread utama untuk menjalankan intent
                    launch(Dispatchers.Main) {
                        // Nomor WhatsApp tujuan
                        val phoneNumber = "6282268672361"
                        val message = "Halo, saya ingin menanyakan apakah stok barang '$productName' masih tersedia?"

                        // Salin gambar ke file cache dengan nama shared_image.jpg
                        val imageFile = File(applicationContext.cacheDir, "shared_image.jpg")
                        val contentUri: Uri = FileProvider.getUriForFile(
                            applicationContext,
                            "${applicationContext.packageName}.provider",
                            imageFile
                        )

                        // Buat intent untuk mengirim pesan
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "image/*"  // Mengirim gambar
                            putExtra(Intent.EXTRA_STREAM, contentUri)  // Gambar yang diunduh
                            putExtra(Intent.EXTRA_TEXT, message)  // Pesan yang ingin dikirim
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)  // Pastikan URI dapat dibaca
                        }

                        // Pastikan WhatsApp terpasang di perangkat
                        if (shareIntent.resolveActivity(packageManager) != null) {
                            // Menggunakan Intent.ACTION_SEND untuk mengirim gambar dan pesan ke WhatsApp
                            val whatsappIntent = Intent(Intent.ACTION_SEND).apply {
                                setPackage("com.whatsapp")  // Tentukan aplikasi WhatsApp
                                putExtra(Intent.EXTRA_STREAM, contentUri)  // Kirim gambar
                                putExtra(Intent.EXTRA_TEXT, message)  // Kirim pesan
                                type = "image/*"
                            }

                            startActivity(Intent.createChooser(whatsappIntent, "Bagikan ke WhatsApp"))
                        } else {
                            Toast.makeText(this@DetailBarangActivityReseller, "WhatsApp tidak terpasang", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Jika gambar gagal diunduh
                    launch(Dispatchers.Main) {
                        Toast.makeText(this@DetailBarangActivityReseller, "Gagal mengunduh gambar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message to WhatsApp: ${e.message}", e)
            Toast.makeText(this, "Gagal mengirim pesan ke WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }






    private fun displayDescription(description: String) {
        val tvDeskripsi = binding.tvDeskripsiBarangDetailBarangReseller

        if (description.length > 30) {
            val shortDescription = description.substring(0, 30) + "..."
            tvDeskripsi.text = shortDescription
            tvDeskripsi.setOnClickListener {
                // Menampilkan deskripsi lengkap jika di klik
                tvDeskripsi.text = description
                tvDeskripsi.setOnClickListener(null) // Matikan listener setelah deskripsi lengkap ditampilkan
            }
        } else {
            tvDeskripsi.text = description
        }
    }

    // Fungsi untuk share produk dengan gambar dan teks
    private fun shareProductWithImage(imageUrl: String, productName: String) {
        try {
            // Jalankan pengunduhan gambar di thread background
            GlobalScope.launch(Dispatchers.IO) {
                val imageUri = downloadImageToCache(imageUrl)
                if (imageUri != null) {
                    // Kirim hasilnya kembali ke thread utama untuk menjalankan intent
                    launch(Dispatchers.Main) {
                        val imageFile = File(applicationContext.cacheDir, "shared_image.jpg")
                        // Salin gambar ke file tersebut jika perlu, misalnya menggunakan InputStream dan OutputStream

                        val contentUri: Uri = FileProvider.getUriForFile(
                            applicationContext,
                            "${applicationContext.packageName}.provider",
                            imageFile
                        )

                        // Buat intent untuk berbagi gambar dan teks
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_STREAM, contentUri)
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Cek barang ini di aplikasi CV Abang Putra: $productName"
                            )
                            type = "image/jpeg"  // Ganti dengan tipe MIME yang sesuai
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)  // Beri izin untuk akses URI
                        }

                        startActivity(Intent.createChooser(shareIntent, "Bagikan Produk"))
                    }
                } else {
                    launch(Dispatchers.Main) {
                        Toast.makeText(
                            this@DetailBarangActivityReseller,
                            "Gagal mengunduh gambar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sharing product image: ${e.message}", e)
            Toast.makeText(this, "Gagal berbagi gambar", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk mengunduh gambar dari URL dan menyimpannya ke cache
    private fun downloadImageToCache(imageUrl: String): Uri? {
        return try {
            val url = URL(imageUrl)
            val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())

            val file = File(cacheDir, "shared_image.jpg")
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }

            Uri.fromFile(file)
        } catch (e: IOException) {
            Log.e(TAG, "Error downloading image: ${e.message}", e)
            null
        }
    }
}