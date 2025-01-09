package com.pinaaa.cvabangputra.reseller.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.databinding.FragmentProfilResellerBinding
import com.pinaaa.cvabangputra.reseller.ui.FeedbackActivityReseller
import com.pinaaa.cvabangputra.reseller.ui.UbahDataActivityReseller
import java.text.SimpleDateFormat
import java.util.Locale

class ProfilFragmentReseller : Fragment() {

    private var _binding: FragmentProfilResellerBinding? = null
    private val binding get() = _binding!!

    // Variabel global sharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

    val apiConfig = ApiConfig()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfilResellerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi sharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("USER", MODE_PRIVATE)

        // Contoh pemanggilan sharedPreferences yang telah diinisialisasi
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

        val formattedTanggalLahir = formatTanggalLahir(tanggalLahir)


        with(binding) {
            tvNamaProfilReseller.text = nama
            tvEmailProfilReseller.text = email
            tvNomorTeleponProfilReseller.text = nomorTelepon
            tvTanggalLahirProfilReseller.text = formattedTanggalLahir
            tvPasswordProfilReseller.text = password
            tvAlamatProfilReseller.text = alamat

            Glide.with(this@ProfilFragmentReseller)
                .load(apiConfig.URL+fotoProfil)
                .centerCrop()
                .into(ivFotoProfilReseller)

            btnKirimFeedbackProfilReseller.setOnClickListener {
                Intent(requireActivity(), FeedbackActivityReseller::class.java).apply {
                    startActivity(this)
                }

            }

            btnUbahDataProfilReseller.setOnClickListener {
                Intent(requireActivity(), UbahDataActivityReseller::class.java).apply {
                    startActivity(this)
                }
            }

        }

        // Log data untuk debugging
        Log.d(TAG, "email: $email")
        Log.d(TAG, "password: $password")
        Log.d(TAG, "role: $role")
        Log.d(TAG, "id_reseller: $idReseller")
        Log.d(TAG, "user_id: $userId")
        Log.d(TAG, "nama: $nama")
        Log.d(TAG, "alamat: $alamat")
        Log.d(TAG, "nomor_telepon: $nomorTelepon")
        Log.d(TAG, "tanggal_lahir: $tanggalLahir")
        Log.d(TAG, "foto_profil: $fotoProfil")
        Log.d(TAG, "isLoggedInReseller: $isLoggedInReseller")

        // Menambahkan fungsi logout
        binding.btnLogoutReseller.setOnClickListener {
            sharedPreferences.edit().clear().apply() // Clear sharedPreferences
            requireActivity().finish() // Menutup activity setelah logout
        }
    }

    // Fungsi untuk format tanggal lahir
    private fun formatTanggalLahir(tanggalLahir: String?): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")) // Format tanggal dd-bulan-yyyy
            val date = inputFormat.parse(tanggalLahir)
            date?.let { outputFormat.format(it) } ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
