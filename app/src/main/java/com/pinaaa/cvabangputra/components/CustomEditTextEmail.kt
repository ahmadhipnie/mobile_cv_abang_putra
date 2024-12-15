package com.pinaaa.cvabangputra.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.pinaaa.cvabangputra.R

class CustomEditTextEmail(context: Context, attrs: AttributeSet? = null) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var clearButtonImage: Drawable? = null

    init {
        init()
    }

    private fun init() {
        // Set input type for email
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        // Set clear button image
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close)
        setOnTouchListener(this)

        // Add text watcher for email validation
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    error = "Email tidak valid"
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }
        })
    }

    // Override onDraw to set the text alignment
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    // Show the clear button when the text is not empty
    private fun showClearButton() {
        setButtonDrawables(null, null, clearButtonImage, null)
    }

    // Hide the clear button when the text is empty
    private fun hideClearButton() {
        setButtonDrawables(null, null, null, null)
    }

    // Set compound drawables (used for clear button)
    private fun setButtonDrawables(start: Drawable?, top: Drawable?, end: Drawable?, bottom: Drawable?) {
        setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom)
    }

    // Handle touch event for the clear button
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val drawableEnd = compoundDrawables[2]
        if (drawableEnd != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = clearButtonImage?.intrinsicWidth?.toFloat() ?: 0f + paddingStart
                if (event.x < clearButtonEnd) {
                    isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - (clearButtonImage?.intrinsicWidth ?: 0)).toFloat()
                if (event.x > clearButtonStart) {
                    isClearButtonClicked = true
                }
            }

            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close)
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close)
                        text?.clear()
                        hideClearButton()
                        return true
                    }
                }
            } else {
                return false
            }
        }
        return false
    }
}