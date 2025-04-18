package com.pinaaa.cvabangputra.admin.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.admin.adapter.KategoriAdminAdapter
import com.pinaaa.cvabangputra.admin.ui.BarangActivityAdmin
import com.pinaaa.cvabangputra.admin.ui.PromoActivityAdmin
import com.pinaaa.cvabangputra.admin.ui.TambahKategoriActivityAdmin
import com.pinaaa.cvabangputra.admin.viewmodel.BarangAdminViewModel
import com.pinaaa.cvabangputra.data.remote.ApiConfig
import com.pinaaa.cvabangputra.databinding.FragmentBerandaAdminBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class BerandaFragmentAdmin : Fragment() {

    private var _binding: FragmentBerandaAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private val apiConfig = ApiConfig()

    private val coroutineScope: CoroutineScope = MainScope()

    private val barangAdminViewModel by viewModels<BarangAdminViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBerandaAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("USER", MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")
        binding.tvDescMenu1BerandaAdmin.text = email

        binding.cvBarangBerandaAdmin.setOnClickListener {
            Intent(requireActivity(), BarangActivityAdmin::class.java).apply {
                startActivity(this)
            }
        }

        binding.cvPromoBerandaAdmin.setOnClickListener {
            Intent(requireActivity(), PromoActivityAdmin::class.java).apply {
                startActivity(this)
            }
        }

        binding.fabTambahBarangBerandaAdmin.setOnClickListener {
            Intent(requireActivity(), TambahKategoriActivityAdmin::class.java).apply {
                startActivity(this)
            }
        }



        barangAdminViewModel.fetchKategori()
        // Observasi kategori dari ViewModel
        barangAdminViewModel.kategoriList.observe(requireActivity()) { kategoriList ->
            // Set up RecyclerView dengan kategori yang diterima
            val kategoriAdminAdapter = KategoriAdminAdapter()
            binding.rvKategoriBerandaAdmin.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = kategoriAdminAdapter
                setHasFixedSize(true)
            }
            kategoriAdminAdapter.submitList(kategoriList)

        }

    }




}