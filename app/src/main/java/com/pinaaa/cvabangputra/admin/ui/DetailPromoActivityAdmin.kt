package com.pinaaa.cvabangputra.admin.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.admin.viewmodel.PromoAdminViewModel
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.databinding.ActivityDetailPromoAdminBinding
import com.pinaaa.cvabangputra.reseller.viewmodel.BerandaResellerViewModel
import com.pinaaa.cvabangputra.reseller.viewmodel.DetailPromoResellerViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailPromoActivityAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPromoAdminBinding

    private val promoAdminViewModel by viewModels<PromoAdminViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val detailPromoResellerViewModel by viewModels<DetailPromoResellerViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val apiConfig = ApiConfig()  // Membuat instance ApiConfig untuk mengakses URL base


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailPromoAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idPromo = intent.getIntExtra("idPromo", 0)
        val namaPromo = intent.getStringExtra("namaPromo")
        val tanggalPeriodeAwal = intent.getStringExtra("tanggalPeriodeAwal")
        val tanggalPeriodeAkhir = intent.getStringExtra("tanggalPeriodeAkhir")
        val gambarUrl = intent.getStringExtra("gambarUrl")
        val createdAt = intent.getStringExtra("created_at")
        val updatedAt = intent.getStringExtra("updated_at")
        val deskripsiPromo = intent.getStringExtra("deskripsiPromo")

        binding.tvNamaPromoDetailPromoAdmin.text = namaPromo
        binding.tvPeriodePromoDetailPromoAdmin.text = "${tanggalPeriodeAwal?.let { formatTanggal(it) }} - ${tanggalPeriodeAkhir?.let {
            formatTanggal(
                it
            )
        }}"

        binding.tvDeskripsiPromoDetailPromoAdmin.text = deskripsiPromo

        binding.btnBackDetailPromoAdmin.setOnClickListener {
            finish()
        }

        binding.btnHapusDetailPromoAdmin.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Promo")
                .setMessage("Apakah Anda yakin ingin menghapus promo ini?")
                .setPositiveButton("Ya") { _, _ ->
                    promoAdminViewModel.deletePromo(idPromo)
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        promoAdminViewModel.deletePromoStatus.observe(this) { isDeleted ->
            if (isDeleted) {
                Toast.makeText(this, "Promo deleted successfully", Toast.LENGTH_SHORT).show()
                finish() // Kembali ke halaman sebelumnya setelah berhasil dihapus
            } else {
                Toast.makeText(this, "Failed to delete promo", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBackDetailPromoAdmin.setOnClickListener {
            finish()
        }

        detailPromoResellerViewModel.getImagesPromoByIdPromo(idPromo)
        detailPromoResellerViewModel.gambarPromo.observe(this) { images ->
            if (!images.isNullOrEmpty()) {
                val imageList = images.mapNotNull { item ->
                    item.gambarUrl?.let {
                        val fullImageUrl = apiConfig.URL + it
                        SlideModel(fullImageUrl, ScaleTypes.CENTER_CROP)
                    }
                }
                binding.imageSliderDetailPromoAdmin.setImageList(imageList)
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

    private fun formatTanggal(dateString: String): String {
        return try {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputDateFormat.parse(dateString)
            val outputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
            outputDateFormat.format(date ?: Date())
        } catch (e: Exception) {
            dateString // Jika gagal memformat, gunakan string asli
        }
    }
}