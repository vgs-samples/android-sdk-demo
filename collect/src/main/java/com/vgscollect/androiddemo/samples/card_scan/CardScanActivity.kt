package com.vgscollect.androiddemo.samples.card_scan

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.microblink.blinkcard.MicroblinkSDK
import com.verygoodsecurity.api.blinkcard.VGSBlinkCardIntentBuilder
import com.verygoodsecurity.vgscollect.core.Environment
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscollect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscollect.widget.PersonNameEditText
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.vgscollect.androiddemo.R

class CardScanActivity : AppCompatActivity(R.layout.activity_card_scan) {

    companion object {

        // Your VGS tenant id and Microblink licence key
        private const val TENANT_ID = ""
        private const val MICROBLINK_LICENSE_KEY = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make sure you added your licence key
        if (MICROBLINK_LICENSE_KEY.isEmpty()) return

        // Init Microblink
        MicroblinkSDK.setLicenseKey(MICROBLINK_LICENSE_KEY, this)

        // Init VGSCollect
        val collect = VGSCollect(this, TENANT_ID, Environment.SANDBOX)

        // Init view
        val vgsCardHolderNameEt = findViewById<PersonNameEditText>(R.id.vgsCardHolderName)
        val vgsCardNumberEt = findViewById<VGSCardNumberEditText>(R.id.vgsCardNumber)
        val vgsCardExpiryEt = findViewById<ExpirationDateEditText>(R.id.vgsCardExpiry)
        val vgsCardCvcEt = findViewById<CardVerificationCodeEditText>(R.id.vgsCardCvc)

        val scanBtn = findViewById<MaterialButton>(R.id.mbScan)

        // Bind VGS view to VGSCollect
        collect.bindView(vgsCardHolderNameEt, vgsCardNumberEt, vgsCardExpiryEt, vgsCardCvcEt)

        // Create scan intent
        val scanIntent = VGSBlinkCardIntentBuilder(this)
            .setCardHolderFieldName(vgsCardHolderNameEt.getFieldName())
            .setCardNumberFieldName(vgsCardNumberEt.getFieldName())
            .setExpirationDateFieldName(vgsCardExpiryEt.getFieldName())
            .setCVCFieldName(vgsCardCvcEt.getFieldName()).build()

        // Create ActivityResultLauncher or use startForActivityResult with onActivityResult callback
        val resultCallback =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                collect.onActivityResult(0, it.resultCode, it.data)
            }

        // Start scanning
        scanBtn.setOnClickListener { resultCallback.launch(scanIntent) }
    }
}