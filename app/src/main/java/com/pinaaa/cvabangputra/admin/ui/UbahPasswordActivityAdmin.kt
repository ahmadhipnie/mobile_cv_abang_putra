package com.pinaaa.cvabangputra.admin.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.admin.viewmodel.BarangAdminViewModel
import com.pinaaa.cvabangputra.admin.viewmodel.UbahPasswordAdminViewModel
import com.pinaaa.cvabangputra.databinding.ActivityUbahPasswordAdminBinding

class UbahPasswordActivityAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityUbahPasswordAdminBinding

    private val ubahPasswordAdminViewModel: UbahPasswordAdminViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUbahPasswordAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE)

        val password = sharedPreferences.getString("password", "")

        binding.btnBackUbahPasswordAdmin.setOnClickListener {
            finish()
        }

        binding.btnUbahPasswordAdmin.setOnClickListener {
            val idUser = sharedPreferences.getInt("id_user", 0)
            val passwordLama = binding.etPasswordLamaUbahPasswordAdmin.text.toString()
            val passwordBaru = binding.etPasswordBaruUbahPasswordAdmin.text.toString()
            val konfirmasiPasswordBaru = binding.etKonfirmasiPasswordBaruUbahPasswordAdmin.text.toString()

            if (passwordLama.isEmpty() || passwordBaru.isEmpty() || konfirmasiPasswordBaru.isEmpty()) {
                binding.etPasswordLamaUbahPasswordAdmin.error = "Field ini tidak boleh kosong"
                binding.etPasswordBaruUbahPasswordAdmin.error = "Field ini tidak boleh kosong"
                binding.etKonfirmasiPasswordBaruUbahPasswordAdmin.error = "Field ini tidak boleh kosong"
            } else if (passwordBaru != konfirmasiPasswordBaru) {
                binding.etKonfirmasiPasswordBaruUbahPasswordAdmin.error = "Password tidak sama"
            } else if (passwordLama != password) {
                binding.etPasswordLamaUbahPasswordAdmin.error = "Password lama salah"
            } else {
                ubahPasswordAdminViewModel.updatePassword(idUser, passwordBaru)
                sharedPreferences.edit().putString("password", passwordBaru).apply()
            }


        }



        ubahPasswordAdminViewModel.updatePasswordStatus.observe(this) { isUpdated ->
            if (isUpdated) {
                Toast.makeText(this, "password updated successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivityAdmin::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish() // Kembali ke halaman sebelumnya setelah berhasil dihapus
            } else {
                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
            }
        }


    }
}