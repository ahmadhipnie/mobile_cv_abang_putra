package com.pinaaa.cvabangputra.admin.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pinaaa.cvabangputra.LoginActivity
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.admin.ui.TransaksiAdminActivity
import com.pinaaa.cvabangputra.admin.ui.UbahPasswordActivityAdmin
import com.pinaaa.cvabangputra.databinding.FragmentProfilAdminBinding

class ProfilFragmentAdmin : Fragment() {

    private var _binding: FragmentProfilAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfilAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("USER", MODE_PRIVATE)

        val email = sharedPreferences.getString("email", "")
        val idUser = sharedPreferences.getInt("id_user", 0)
        val password = sharedPreferences.getString("password", "")

        binding.tvEmailProfilAdminValue.text = email
        binding.tvPasswordProfilAdminValue.text = password


        binding.btnUbahPasswordProfilAdmin.setOnClickListener{
            val intent = Intent(requireActivity(), UbahPasswordActivityAdmin::class.java)
            startActivity(intent)
        }

        binding.btnRiwayatTransaksiProfilAdmin.setOnClickListener {
            Intent(requireActivity(), TransaksiAdminActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnLogoutProfilAdmin.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            Intent(requireActivity(), LoginActivity::class.java).also {
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                requireActivity().finish()
            }
        }





    }
}