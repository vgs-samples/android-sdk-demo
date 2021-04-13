package com.vgscollect.androiddemo.usecase.payments.checkout

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.vgscollect.androiddemo.R
import kotlinx.android.synthetic.main.activity_payments_checkout_default_components.*
import java.text.SimpleDateFormat
import java.util.*

class NativeCheckoutFormActivity : AppCompatActivity() {

    private val dateFormatter = SimpleDateFormat("MM/yy", Locale.getDefault())

    private val inputViews: List<TextInputEditText> by lazy {
        listOf(etCardNumber, etCardHolderName, etDate, etCVC)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments_checkout_default_components)
        initViews()
        initListeners()
    }

    private fun initViews() {
        etDate?.inputType = InputType.TYPE_NULL
    }

    private fun initListeners() {
        etCardNumber?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.trim()?.length == MAX_CARD_NUMBER_LENGTH) {
                    etCardHolderName?.requestFocus()
                }
            }
        })
        etDate?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                etCVC?.requestFocus()
            }
        })
        etDate?.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.hideKeyboard()
                showDateDialog()
            }
        }
        mbAddCard?.setOnClickListener {
            if (!inputViews.any { it.text.isNullOrEmpty() }) {
                Toast.makeText(this, "Card added!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDateDialog() {
        Handler(Looper.getMainLooper()).postDelayed({
            MaterialDatePicker.Builder.datePicker()
                .setSelection(currentTimeMillis)
                .setCalendarConstraints(
                    CalendarConstraints.Builder().setStart(currentTimeMillis).build()
                )
                .build().also {
                    it.addOnCancelListener {
                        etDate?.clearFocus()
                    }
                    it.addOnNegativeButtonClickListener {
                        etDate?.clearFocus()
                    }
                    it.addOnPositiveButtonClickListener { time ->
                        etDate?.setText(dateFormatter.format(Date(time)))
                    }
                }
                .show(supportFragmentManager, null)
        }, 150)
    }

    val currentTimeMillis: Long
        get() = System.currentTimeMillis()

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    companion object {
        private const val MAX_CARD_NUMBER_LENGTH = 16
    }
}