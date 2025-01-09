package com.pinaaa.cvabangputra.reseller.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.app.ProgressDialog
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.databinding.ActivityFeedbackResellerBinding
import com.pinaaa.cvabangputra.reseller.viewmodel.KirimFeedbackResellerViewModel
import kotlinx.coroutines.launch

class FeedbackActivityReseller : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackResellerBinding
    private lateinit var progressDialog: ProgressDialog  // Mendeklarasikan ProgressDialog

    private val kirimFeedbackResellerViewModel by viewModels<KirimFeedbackResellerViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackResellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi ProgressDialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Mengirim feedback...")
            setCancelable(false) // Tidak dapat dibatalkan (user tidak bisa membatalkan saat loading)
        }

        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE)

        val email = sharedPreferences.getString("email", "") ?: ""
        val namaReseller = sharedPreferences.getString("nama", "") ?: ""

        // Set event listener for button "Kirim Feedback"
        binding.btnKirimFeedbackReseller.setOnClickListener {
            val rating = binding.ratingBarFeedbackReseller.rating.toInt()
            val isiFeedback = binding.etFeedbackReseller.text.toString()

            if (isiFeedback.isNotEmpty() && rating > 0) {
                // Menampilkan ProgressDialog sebelum memulai pengiriman feedback
                progressDialog.show()

                // Mengirim feedback ke API
                kirimFeedbackReseller(email, namaReseller, rating, isiFeedback)
            } else {
                // Beri informasi jika feedback atau rating kosong
                Log.e("FeedbackActivity", "Isi feedback atau rating tidak valid")
                Toast.makeText(this@FeedbackActivityReseller, "Rating dan feedback tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBackFeedbackReseller.setOnClickListener {
            finish()
        }
    }

    // Fungsi untuk mengirim feedback ke API
    private fun kirimFeedbackReseller(email: String, namaReseller: String, rating: Int, isiFeedback: String) {
        lifecycleScope.launch {
            try {
                // Mengirim feedback melalui ViewModel
                val response = kirimFeedbackResellerViewModel.kirimFeedbackReseller(email, namaReseller, rating, isiFeedback)

                // Menyembunyikan ProgressDialog setelah API call selesai
                progressDialog.dismiss()

                if (response.status == "success") {
                    // Feedback berhasil dikirim
                    Log.d("FeedbackActivity", "Feedback berhasil dikirim: ${response.message}")
                    Toast.makeText(this@FeedbackActivityReseller, "Feedback berhasil dikirim", Toast.LENGTH_SHORT).show()

                    finish()
                } else {
                    // Jika statusnya bukan success, tampilkan error message
                    Log.e("FeedbackActivity", "Error: ${response.message}")
                    Toast.makeText(this@FeedbackActivityReseller, "Gagal mengirim feedback", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                // Menyembunyikan ProgressDialog jika terjadi kesalahan
                progressDialog.dismiss()

                // Tangani exception jika terjadi error pada API atau lainnya
                Log.e("FeedbackActivity", "Terjadi kesalahan: ${e.message}")
                Toast.makeText(this@FeedbackActivityReseller, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
