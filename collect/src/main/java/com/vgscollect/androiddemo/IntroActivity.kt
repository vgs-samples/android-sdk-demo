package com.vgscollect.androiddemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vgscollect.androiddemo.usecase.payments.checkout.CollectCheckoutFormActivity
import com.vgscollect.androiddemo.usecase.payments.checkout.NativeCheckoutFormActivity
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity: AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        nativeUiBtn?.setOnClickListener(this)
        collectUiBtn?.setOnClickListener(this)
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