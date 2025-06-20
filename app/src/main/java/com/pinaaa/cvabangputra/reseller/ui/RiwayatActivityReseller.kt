package com.pinaaa.cvabangputra.reseller.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.data.remote.response.reseller.RiwayatTransaksiResponse
import com.pinaaa.cvabangputra.databinding.ActivityRiwayatResellerBinding
import com.pinaaa.cvabangputra.reseller.adapter.RiwayatTransaksiResellerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatActivityReseller : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatResellerBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val apiConfig = ApiConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRiwayatResellerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE)

        // Set up the RecyclerView
        val adapter = RiwayatTransaksiResellerAdapter()
        binding.rvRiwayatTransaksiReseller.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        binding.rvRiwayatTransaksiReseller.layoutManager = layoutManager


        binding.btnBackRiwayatReseller.setOnClickListener { finish() }

        val userId = sharedPreferences.getString("user_id", "")?.toInt() ?: 0
        if (userId != 0) {
            // Fetch data from the API
            fetchData(userId, adapter)
        } else {
            // Handle case where user_id is not available
            Toast.makeText(this, "User ID is missing!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchData(userId: Int, adapter: RiwayatTransaksiResellerAdapter) {
        // Example of calling your API to get the data
        val apiService = apiConfig.getApiService()

        apiService.getTransaksiByUserId(userId).enqueue(object : Callback<RiwayatTransaksiResponse> {
            override fun onResponse(
                call: Call<RiwayatTransaksiResponse>,
                response: Response<RiwayatTransaksiResponse>
            ) {
                if (response.isSuccessful) {
                    val transaksiList = response.body()?.data ?: emptyList()
                    adapter.submitList(transaksiList)  // Set the data to the adapter
                } else {
                    Toast.makeText(this@RiwayatActivityReseller, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RiwayatTransaksiResponse>, t: Throwable) {
                Toast.makeText(this@RiwayatActivityReseller, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
