package com.vgscollect.androiddemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.vgscollect.androiddemo.usecase.payments.checkout.CollectCheckoutFormActivity
import com.vgscollect.androiddemo.usecase.payments.checkout.NativeCheckoutFormActivity

class IntroActivity: AppCompatActivity(), View.OnClickListener {

    private val nativeUiBtn: Button by lazy { findViewById(R.id.nativeUiBtn) }
    private val collectUiBtn: Button by lazy { findViewById(R.id.collectUiBtn) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        nativeUiBtn.setOnClickListener(this)
        collectUiBtn.setOnClickListener(this)
    }

    override fun onClick(view:View?) {
        when(view?.id) {
            R.id.nativeUiBtn -> runNativeUI()
            R.id.collectUiBtn -> runCollectUI()
        }
    }

    private fun runCollectUI() {
        with(Intent(this, CollectCheckoutFormActivity::class.java)) {
            startActivity(this)
        }
    }

    private fun runNativeUI() {
        with(Intent(this, NativeCheckoutFormActivity::class.java)) {
            startActivity(this)
        }
    }
}