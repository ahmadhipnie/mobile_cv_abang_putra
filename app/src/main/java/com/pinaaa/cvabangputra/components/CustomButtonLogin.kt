package com.pinaaa.cvabangputra.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.pinaaa.cvabangputra.R

class CustomButtonLogin(context: Context, attrs: AttributeSet? = null) : AppCompatButton(context, attrs) {

    private var txtColor: Int
    private var enabledBackground: Drawable?
    private var disabledBackground: Drawable?

    init {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)

        // Menggunakan drawable dengan rounded corners
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button)  // Pastikan ini merujuk ke drawable yang baru
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disabled)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Set background based on enabled state
        background = if (isEnabled) enabledBackground else disabledBackground

        // Set text color and other properties
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER

        // Set button text based on enabled state
        text = if (isEnabled) "Login" else "Harap diisi"
    }
}