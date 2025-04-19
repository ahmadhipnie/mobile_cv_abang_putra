package com.pinaaa.cvabangputra.reseller.fragment
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.pinaaa.cvabangputra.databinding.FragmentFullScreenImageBinding

class FullScreenImageFragment : DialogFragment() {

    private lateinit var binding: FragmentFullScreenImageBinding

    companion object {
        private const val IMAGE_URL = "image_url"

        // Factory method untuk membuat instance dengan gambar URL
        fun newInstance(imageUrl: String): FullScreenImageFragment {
            val fragment = FullScreenImageFragment()
            val bundle = Bundle()
            bundle.putString(IMAGE_URL, imageUrl)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFullScreenImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil URL gambar dari arguments
        val imageUrl = arguments?.getString(IMAGE_URL) ?: ""

        // Memuat gambar dalam mode full-screen menggunakan Glide
        Glide.with(requireContext())
            .load(imageUrl)
            .into(binding.fullScreenImageView)

        // Menambahkan listener untuk menutup gambar full-screen ketika diklik
        binding.fullScreenImageView.setOnClickListener {
            dismiss()  // Menutup fragment ketika gambar diklik
        }
    }
}
