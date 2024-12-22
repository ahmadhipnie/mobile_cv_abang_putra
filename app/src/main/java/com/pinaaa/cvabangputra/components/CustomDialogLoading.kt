package com.pinaaa.cvabangputra.components

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import com.pinaaa.cvabangputra.R

class CustomDialogLoading(context: Context) : Dialog(context) {

    init {
        // Atur atribut jendela
        val params = window!!.attributes
        params.gravity = Gravity.CENTER
        window!!.attributes = params
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Atur dialog
        setTitle(null)
        setCancelable(true)
        setOnCancelListener(null)

        // Inflate dan set layout konten
        val view = LayoutInflater.from(context).inflate(R.layout.loading_layout, null)
        setContentView(view)
    }

    // Metode untuk menampilkan atau menyembunyikan dialog
    fun setLoadingVisible(isVisible: Boolean) {
        if (isVisible) {
            show()
        } else {
            dismiss()
        }
    }
}
