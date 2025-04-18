package com.pinaaa.cvabangputra.admin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.pinaaa.cvabangputra.ViewModelFactory
import com.pinaaa.cvabangputra.admin.adapter.StokBarangAdminAdapter
import com.pinaaa.cvabangputra.admin.viewmodel.BarangAdminViewModel
import com.pinaaa.cvabangputra.admin.viewmodel.StokBarangViewModel
import com.pinaaa.cvabangputra.databinding.FragmentLaporanAdminBinding

class LaporanFragmentAdmin : Fragment() {

    private var _binding: FragmentLaporanAdminBinding? = null
    private val binding get() = _binding!!

    private val stokBarangViewModel by viewModels<StokBarangViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val barangAdminViewModel: BarangAdminViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var adapter: StokBarangAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLaporanAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi adapter terlebih dahulu
        adapter = StokBarangAdminAdapter(stokBarangViewModel)

        // Setup recyclerView & observer
        setupRecyclerView()
        observeViewModel()

        // Ambil data
        barangAdminViewModel.fetchBarangs()
    }

    private fun setupRecyclerView() {
        binding.rvStokBarangAdmin.layoutManager = GridLayoutManager(requireActivity(), 1)
        binding.rvStokBarangAdmin.adapter = adapter
    }

    private fun observeViewModel() {
        // Gunakan viewLifecycleOwner yang aman
        barangAdminViewModel.barangList.observe(viewLifecycleOwner) { barangList ->
            adapter.submitList(barangList)
        }

        barangAdminViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarLaporanAdmin.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
