package com.pinaaa.cvabangputra.reseller.ui

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.FeedbackResponse
import com.pinaaa.cvabangputra.databinding.ActivityPesanSekarangResellerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.toString

class PesanSekarangActivityReseller : AppCompatActivity() {
    private lateinit var binding: ActivityPesanSekarangResellerBinding

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPesanSekarangResellerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE)

        // Dapatkan data dari Intent
        val idBarang = intent.getIntExtra("idBarang", 0)
        val hargaBarang = intent.getIntExtra("hargaBarang", 0)
        val userId = sharedPreferences.getString("user_id", "")?.toInt() ?: 0

        Log.d(TAG, "onCreate: $hargaBarang, $idBarang, $userId")

        // Menambahkan data ke spinner
        val pengirimanOptions = arrayOf("cod", "ambil_ditempat")  // Daftar jenis pengiriman
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pengirimanOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPengiriman.adapter = adapter


        // Menampilkan harga barang di layout (jika diperlukan)
        binding.etNamaBarangPesanSekarangReseller.setText(intent.getStringExtra("namaBarang"))


        binding.etJumlahBarangPesanSekarangReseller.addTextChangedListener { text ->
            val jumlahBarangStr = text.toString()
            val jumlahBarang = jumlahBarangStr.toIntOrNull()

            if (jumlahBarang != null) {
                // Hitung total harga
                val totalHarga = jumlahBarang * hargaBarang
                binding.etTotalHargaPesanSekarangReseller.setText(totalHarga.toString())
            } else {
                binding.etTotalHargaPesanSekarangReseller.setText("")
            }
        }

        // Kembali ke halaman sebelumnya
        binding.btnBackPesanSekarangAdmin.setOnClickListener {
            finish()
        }

        binding.btnPesanSekarangReseller.setOnClickListener {
            val jumlahBarangStr = binding.etJumlahBarangPesanSekarangReseller.text.toString()
            val totalHargaStr = binding.etTotalHargaPesanSekarangReseller.text.toString()
            val alamatPengiriman = binding.etAlamatPengirimanPesanSekarangReseller.text.toString()
            val jenisPengiriman = binding.spinnerPengiriman.selectedItem.toString()

            if (userId == 0 || idBarang == 0 || jumlahBarangStr.isEmpty() || totalHargaStr.isEmpty()) {
                Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val jumlahBarang = jumlahBarangStr.toIntOrNull()
            val totalHarga = totalHargaStr.toIntOrNull()

            if (jumlahBarang == null || totalHarga == null) {
                Toast.makeText(this, "Jumlah dan harga harus berupa angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Memproses transaksi...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val apiService = ApiConfig.getApiService()
            apiService.addTransaksi(userId, idBarang, jumlahBarang,jenisPengiriman, alamatPengiriman, totalHarga)
                .enqueue(object : Callback<FeedbackResponse> {
                    override fun onResponse(call: Call<FeedbackResponse>, response: Response<FeedbackResponse>) {
                        progressDialog.dismiss()
                        if (response.isSuccessful && response.body()?.status == "success") {
                            Toast.makeText(this@PesanSekarangActivityReseller, "Transaksi berhasil", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@PesanSekarangActivityReseller, response.body()?.message ?: "Transaksi gagal", Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "onResponse: " + response.body()?.message )
                        }
                    }

                    override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(this@PesanSekarangActivityReseller, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
