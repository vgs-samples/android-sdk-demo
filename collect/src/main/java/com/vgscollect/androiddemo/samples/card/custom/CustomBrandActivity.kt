package com.vgscollect.androiddemo.samples.card.custom

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.view.card.BrandParams
import com.verygoodsecurity.vgscollect.view.card.CardBrand
import com.verygoodsecurity.vgscollect.view.card.validation.payment.ChecksumAlgorithm
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.vgscollect.androiddemo.R

class CustomBrandActivity : AppCompatActivity() {

    private lateinit var vgsCollect: VGSCollect

    private val rootView: FrameLayout by lazy { findViewById(R.id.rootView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        // Init collect
        vgsCollect = VGSCollect(this, "<vault_id>", "<environment>")

        // Setup view
        val vgsEtCardNumber = VGSCardNumberEditText(this).apply {
            setFieldName("<field_name>")
            setHint("Card number")
            setDivider('-')
            setCardBrandIconGravity(Gravity.END)
            setPadding(resources.getDimensionPixelSize(R.dimen.padding))
        }
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
            "<regex_to_detect_the_brand>",
            "Custom Brand",
            R.drawable.ic_visa_dark,
            brandParams
        )

        // Add custom brand
        vgsEtCardNumber.addCardBrand(cardBrand)
    }
}