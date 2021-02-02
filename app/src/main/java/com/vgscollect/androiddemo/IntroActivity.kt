package com.vgscollect.androiddemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.vgscollect.androiddemo.vgs.MainActivity
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity: AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        nativeUiBtn?.setOnClickListener(this)
        collectUiBtn?.setOnClickListener(this)
        showUiBtn?.setOnClickListener(this)
        generalUiBtn?.setOnClickListener(this)
    }

    override fun onClick(view:View?) {
        when(view?.id) {
            R.id.nativeUiBtn -> runNativeUI()
            R.id.collectUiBtn -> runCollectUI()
            R.id.showUiBtn -> runNativeUI()
            R.id.generalUiBtn -> runCollectShowUI()
        }
    }

    private fun runCollectShowUI() {
        with(Intent(this, MainActivity::class.java)) {
            startActivity(this)
        }
    }

    private fun runCollectUI() {
        with(Intent(this, CollectActivity::class.java)) {
            startActivity(this)
        }
    }

    private fun runNativeUI() {
        with(Intent(this, NativeControlsActivity::class.java)) {
            startActivity(this)
        }
    }
}