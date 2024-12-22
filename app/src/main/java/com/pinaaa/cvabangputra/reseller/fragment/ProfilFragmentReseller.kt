package com.pinaaa.cvabangputra.reseller.fragment

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.databinding.FragmentProfilResellerBinding

class ProfilFragmentReseller : Fragment() {

    private var _binding: FragmentProfilResellerBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

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


        sharedPreferences = requireActivity().getSharedPreferences("USER", MODE_PRIVATE)

        binding.btnLogoutReseller.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            requireActivity().finish()
        }

        Log.d(TAG, "email: ${sharedPreferences.getString("email", "")}")
        Log.d(TAG, "password: ${sharedPreferences.getString("password", "")}")
        Log.d(TAG, "role: ${sharedPreferences.getString("role", "")}")
        Log.d(TAG, "id_reseller: ${sharedPreferences.getString("id_reseller", "")}")
        Log.d(TAG, "user_id: ${sharedPreferences.getString("user_id", "")}")
        Log.d(TAG, "nama: ${sharedPreferences.getString("nama", "")}")
        Log.d(TAG, "alamat: ${sharedPreferences.getString("alamat", "")}")
        Log.d(TAG, "nomor_telepon: ${sharedPreferences.getString("nomor_telepon", "")}")
        Log.d(TAG, "tanggal_lahir: ${sharedPreferences.getString("tanggal_lahir", "")}")
        Log.d(TAG, "foto_profil: ${sharedPreferences.getString("foto_profil", "")}")
        Log.d(TAG, "isLoggedInReseller: ${sharedPreferences.getBoolean("isLoggedInReseller", false)}")
    }
}