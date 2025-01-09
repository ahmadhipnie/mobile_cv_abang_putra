package com.pinaaa.cvabangputra.reseller.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.databinding.ActivityDetailPromoResellerBinding
import com.pinaaa.cvabangputra.reseller.viewmodel.DetailBarangResellerViewModel
import com.pinaaa.cvabangputra.reseller.viewmodel.DetailPromoResellerViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class DetailPromoActivityReseller : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPromoResellerBinding

    private val detailPromoResellerViewModel by viewModels<DetailPromoResellerViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val apiConfig = ApiConfig()  // Membuat instance ApiConfig untuk mengakses URL base

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailPromoResellerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val promoId = intent.getIntExtra("promo_id", 0)
        val gambarUrl = intent.getStringExtra("gambar_url")
        val namaPromo = intent.getStringExtra("nama_promo")
        val deskripsiPromo = intent.getStringExtra("deskripsi_promo")
        val tanggalPeriodeAwal = intent.getStringExtra("tanggal_periode_awal")
        val tanggalPeriodeAkhir = intent.getStringExtra("tanggal_periode_akhir")
        val idGambarPromo = intent.getIntExtra("id_gambar_promo", 0)

        // Mengubah tanggalPeriodeAwal dan tanggalPeriodeAkhir menjadi format dd-MMMM-yyyy
        val dateFormatInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateFormatOutput = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))  // Format Indonesia (untuk nama bulan)

        val tanggalAwalFormatted = try {
            val dateAwal = dateFormatInput.parse(tanggalPeriodeAwal)
            dateFormatOutput.format(dateAwal ?: "")
        } catch (e: Exception) {
            e.printStackTrace()
            tanggalPeriodeAwal  // Jika error, tampilkan tanggal asli
        }

        val tanggalAkhirFormatted = try {
            val dateAkhir = dateFormatInput.parse(tanggalPeriodeAkhir)
            dateFormatOutput.format(dateAkhir ?: "")
        } catch (e: Exception) {
            e.printStackTrace()
            tanggalPeriodeAkhir  // Jika error, tampilkan tanggal asli
        }

        with(binding) {
            tvNamaPromoDetailPromoReseller.text = namaPromo
            tvDeskripsiPromoDetailPromoReseller.text = deskripsiPromo
            tvPeriodePromoDetailPromoReseller.text = "$tanggalAwalFormatted - $tanggalAkhirFormatted"
            btnBackDetailPromoReseller.setOnClickListener {
                finish()
            }
        }

        detailPromoResellerViewModel.getImagesPromoByIdPromo(promoId)
        detailPromoResellerViewModel.gambarPromo.observe(this) { images ->
            if (!images.isNullOrEmpty()) {
                val imageList = images.mapNotNull { item ->
                    item.gambarUrl?.let {
                        val fullImageUrl = apiConfig.URL + it
                        SlideModel(fullImageUrl, ScaleTypes.CENTER_CROP)
                    }
                }
                binding.imageSliderDetailPromoReseller.setImageList(imageList)
            } else {
                Toast.makeText(this, "Tidak ada gambar untuk barang ini.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        detailPromoResellerViewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            Log.e(TAG, "onCreate: $errorMessage")
        }
    }
}
