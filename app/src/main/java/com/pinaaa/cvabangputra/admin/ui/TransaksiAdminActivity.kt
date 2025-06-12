package com.pinaaa.cvabangputra.admin.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.admin.adapter.RiwayatTransaksiAdminAdapter
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.RiwayatTransaksiResponse
import com.pinaaa.cvabangputra.databinding.ActivityTransaksiAdminBinding
import com.pinaaa.cvabangputra.reseller.adapter.RiwayatTransaksiResellerAdapter
import com.pinaaa.cvabangputra.reseller.ui.RiwayatActivityReseller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransaksiAdminActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTransaksiAdminBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val apiConfig = ApiConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTransaksiAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBackTransaksiAdmin.setOnClickListener {
            finish()
        }

        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE)

        // Set up the RecyclerView
        val adapter = RiwayatTransaksiAdminAdapter()
        binding.rvRiwayatTransaksiAdmin.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        binding.rvRiwayatTransaksiAdmin.layoutManager = layoutManager

        fetchData(adapter)


    }
    private fun fetchData(adapter: RiwayatTransaksiAdminAdapter) {
        // Example of calling your API to get the data
        val apiService = apiConfig.getApiService()

        apiService.getAllTransaksi().enqueue(object : Callback<RiwayatTransaksiResponse> {
            override fun onResponse(
                call: Call<RiwayatTransaksiResponse>,
                response: Response<RiwayatTransaksiResponse>
            ) {
                if (response.isSuccessful) {
                    val transaksiList = response.body()?.data ?: emptyList()
                    adapter.submitList(transaksiList)  // Set the data to the adapter
                } else {
                    Toast.makeText(this@TransaksiAdminActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RiwayatTransaksiResponse>, t: Throwable) {
                Toast.makeText(this@TransaksiAdminActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}