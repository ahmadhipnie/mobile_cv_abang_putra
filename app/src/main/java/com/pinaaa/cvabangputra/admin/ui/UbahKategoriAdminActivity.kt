package com.pinaaa.cvabangputra.admin.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.databinding.ActivityUbahKategoriAdminBinding
import com.pinaaa.cvabangputra.di.injection.reduceFileImage
import com.pinaaa.cvabangputra.di.injection.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class UbahKategoriAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUbahKategoriAdminBinding

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.civFotoKategoriUbahAdmin.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUbahKategoriAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        showLoading(false)


        val idKategori = intent.getIntExtra("idKategori", 0)
        val namaKategori = intent.getStringExtra("namaKategori")
        val imageUrl = intent.getStringExtra("imageUrl")

        Glide.with(this)
            .load(ApiConfig().URL + imageUrl)
            .centerCrop()
            .into(binding.civFotoKategoriUbahAdmin)

        binding.etNamaKategoriUbahAdmin.setText(namaKategori)



        binding.btnUbahKategoriAdmin.setOnClickListener{
            finish()
        }

        binding.btnBackUbahKategoriAdmin.setOnClickListener {
            finish()
        }

        binding.civFotoKategoriUbahAdmin.setOnClickListener {
            startGallery()
        }

        binding.btnUbahKategoriAdmin.setOnClickListener {
            showLoading(true)
            updateKategori(idKategori)
        }
    }

    private fun updateKategori(idKategori:Int) {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()  // Mengambil gambar dari Uri dan mengkompresnya
            Log.d("Image File", "addKategori: ${imageFile.path}")

            // Membuat RequestBody untuk data kategori
            val namaKategori = binding.etNamaKategoriUbahAdmin.text.toString()

            // Validasi input
            if (namaKategori.isEmpty()) {
                showToast("Nama kategori tidak boleh kosong")
                return
            }

            val requestBodyMap = mapOf(
                "nama_kategori" to namaKategori.toRequestBody("text/plain".toMediaType()),
                "id_kategori" to idKategori.toString().toRequestBody("text/plain".toMediaType())
                )

            // Membuat MultipartBody.Part untuk gambar
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image_url",
                imageFile.name,
                requestImageFile
            )

            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig().getApiService()
                    val response = apiService.updateKategori(requestBodyMap, multipartBody)

                    if (response.status == "success") {
                        showToast("Kategori berhasil ditambahkan")
                        finish() // Kembali ke halaman sebelumnya setelah berhasil
                    } else {
                        showToast("Gagal menambahkan kategori: ${response.message}")
                    }
                } catch (e: HttpException) {
                    Log.e("AddKategori", "HttpException: ${e.message}")
                    showToast("Terjadi kesalahan: ${e.message()}")
                } catch (e: Exception) {
                    Log.e("AddKategori", "Exception: ${e.message}")
                    showToast("Terjadi kesalahan")
                } finally {
                    showLoading(false)
                }
            }
        } ?: showToast("Silakan pilih gambar terlebih dahulu")
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarUbahKategoriAdmin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

}