package com.vgscollect.androiddemo.samples.brands.custom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.view.card.BrandParams
import com.verygoodsecurity.vgscollect.view.card.CardBrand
import com.verygoodsecurity.vgscollect.view.card.validation.payment.ChecksumAlgorithm
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.vgscollect.androiddemo.R
import kotlinx.android.synthetic.main.activity_layout.*

class CustomBrandActivity : AppCompatActivity() {

    private val vgsCollect: VGSCollect by lazy {
        VGSCollect.Builder(this, "<VAULT_ID>").create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        // Setup view
        val vgsEtCardNumber = VGSCardNumberEditText(this)
        vgsEtCardNumber.setFieldName("<FIELD_NAME>")
        vgsEtCardNumber.setHint("Card number")
        vgsEtCardNumber.setDivider('-')
        rootView.addView(vgsEtCardNumber)

        // Subscribe view
        vgsCollect.bindView(vgsEtCardNumber)

        // Create custom brand
        val brandParams = BrandParams(
            "## #### ## #### #### ###",
            ChecksumAlgorithm.LUHN,
            arrayOf(19),
            arrayOf(3)
        )
        val cardBrand = CardBrand(
            "^99",
            "Custom Brand",
            R.drawable.ic_visa_dark,
            brandParams
        )

        // Add custom brand
        vgsEtCardNumber.addCardBrand(cardBrand)
    }
}