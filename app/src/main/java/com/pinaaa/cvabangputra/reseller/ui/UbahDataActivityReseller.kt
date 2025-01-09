package com.pinaaa.cvabangputra.reseller.ui

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.pinaaa.cvabangputra.LoginActivity
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.databinding.ActivityUbahDataResellerBinding
import com.pinaaa.cvabangputra.di.injection.reduceFileImage
import com.pinaaa.cvabangputra.di.injection.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class UbahDataActivityReseller : AppCompatActivity() {
    private lateinit var binding: ActivityUbahDataResellerBinding

    private lateinit var sharedPreferences: SharedPreferences

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
            binding.civFotoProfilUbahDataReseller.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUbahDataResellerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE)

        val email = sharedPreferences.getString("email", "")
        val password = sharedPreferences.getString("password", "")
        val role = sharedPreferences.getString("role", "")
        val idReseller = sharedPreferences.getString("id_reseller", "")
        val userId = sharedPreferences.getString("user_id", "")
        val nama = sharedPreferences.getString("nama", "")
        val alamat = sharedPreferences.getString("alamat", "")
        val nomorTelepon = sharedPreferences.getString("nomor_telepon", "")
        val tanggalLahir = sharedPreferences.getString("tanggal_lahir", "")
        val fotoProfil = sharedPreferences.getString("foto_profil", "")
        val isLoggedInReseller = sharedPreferences.getBoolean("isLoggedInReseller", false)

        binding.etEmailUbahDataReseller.setText(email)
        binding.etNamaUbahDataReseller.setText(nama)
        binding.etAlamatUbahDataReseller.setText(alamat)
        binding.etNomorTeleponUbahDataReseller.setText(nomorTelepon)
        binding.tvNamaUbahDataReseller.text = nama
        binding.etPasswordUbahDataReseller.setText(password)

        binding.civFotoProfilUbahDataReseller.setOnClickListener {
            startGallery()
        }

        binding.btnBackFeedbackReseller.setOnClickListener {
            finish()
        }

        binding.btnUbahDataReseller.setOnClickListener {
            val emailBaru = binding.etEmailUbahDataReseller.text.toString()
            val passwordBaru = binding.etPasswordUbahDataReseller.text.toString()
            val namaBaru = binding.etNamaUbahDataReseller.text.toString()
            val alamatBaru = binding.etAlamatUbahDataReseller.text.toString()
            val nomorTeleponBaru = binding.etNomorTeleponUbahDataReseller.text.toString()
            val konfirmasiPasswordBaru = binding.etKonfirmasiPasswordUbahDataReseller.text.toString()

            // Periksa jika ada field yang kosong
            if (emailBaru.isEmpty() || passwordBaru.isEmpty() || namaBaru.isEmpty() || alamatBaru.isEmpty() || nomorTeleponBaru.isEmpty() || konfirmasiPasswordBaru.isEmpty()) {
                showToast("Please fill all fields")
                return@setOnClickListener
            }

            // Periksa jika gambar profil belum dipilih
            else if (currentImageUri == null) {
                showToast("Please select a profile picture")
                return@setOnClickListener
            }

            // Periksa jika password dan konfirmasi password tidak sama
            else if (passwordBaru != konfirmasiPasswordBaru) {
                showToast("Password and confirmation password must be the same")
                return@setOnClickListener
            }

            // Jika semua validasi lulus, lakukan proses update data
            else {
                showLoading(true)
                updateData()
            }
        }


    }

    private fun updateData() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")

            // Membuat RequestBody untuk data lain
            val emailBaru = binding.etEmailUbahDataReseller.text.toString()
            val passwordBaru = binding.etPasswordUbahDataReseller.text.toString()
            val namaBaru = binding.etNamaUbahDataReseller.text.toString()
            val alamatBaru = binding.etAlamatUbahDataReseller.text.toString()
            val nomorTeleponBaru = binding.etNomorTeleponUbahDataReseller.text.toString()

            val idUser = sharedPreferences.getString("user_id", "").toString()

            // Membuat RequestBody untuk masing-masing field
            val requestBodyMap = mapOf(
                "id_user" to idUser.toRequestBody("text/plain".toMediaType()),
                "email" to emailBaru.toRequestBody("text/plain".toMediaType()),
                "password" to passwordBaru.toRequestBody("text/plain".toMediaType()),
                "nama" to namaBaru.toRequestBody("text/plain".toMediaType()),
                "alamat" to alamatBaru.toRequestBody("text/plain".toMediaType()),
                "nomor_telepon" to nomorTeleponBaru.toRequestBody("text/plain".toMediaType())
            )

            // Membuat MultipartBody.Part untuk foto profil
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "foto_profil",
                imageFile.name,
                requestImageFile
            )

            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig().getApiService()
                    val response = apiService.updateUserReseller(requestBodyMap, multipartBody)

                    // Menampilkan feedback dari response
                    if (response.status == "success") {
                        showToast("Data updated successfully")
                        // Navigate to login or other activity if needed
                        Intent(this@UbahDataActivityReseller, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            sharedPreferences.edit().clear().apply()
                            startActivity(this)
                        }
                    } else {
                        showToast("Update failed: ${response.message}")
                    }

                } catch (e: HttpException) {
                    Log.e("UpdateData", "HttpException: ${e.message}")
                    showToast("Update failed: ${e.message()}")
                } catch (e: Exception) {
                    Log.e("UpdateData", "Exception: ${e.message}")
                    showToast("An error occurred")
                } finally {
                    showLoading(false)
                }
            }
        } ?: showToast("Please select a profile picture")
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicatorResult.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        showLoading(false)
    }
}