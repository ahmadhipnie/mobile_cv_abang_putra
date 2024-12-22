package com.pinaaa.cvabangputra.reseller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pinaaa.cvabangputra.R
import com.pinaaa.cvabangputra.databinding.FragmentSearchResellerBinding

class SearchFragmentReseller : Fragment() {

    private var _binding: FragmentSearchResellerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchResellerBinding.inflate(inflater, container, false)
        return binding.root
    }

}