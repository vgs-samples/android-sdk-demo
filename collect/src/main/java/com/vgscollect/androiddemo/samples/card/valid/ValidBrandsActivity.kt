package com.vgscollect.androiddemo.samples.card.valid

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.util.extension.toCardBrand
import com.verygoodsecurity.vgscollect.view.card.BrandParams
import com.verygoodsecurity.vgscollect.view.card.CardBrand
import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.view.card.validation.payment.ChecksumAlgorithm
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.vgscollect.androiddemo.R
import kotlinx.android.synthetic.main.activity_layout.*

class ValidBrandsActivity : AppCompatActivity() {

    private lateinit var vgsCollect: VGSCollect

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
            "^99",
            "Custom Brand",
            R.drawable.ic_visa_dark,
            brandParams
        )

        // Set valid card brands
        vgsEtCardNumber.setValidCardBrands(cardBrand, CardType.VISA.toCardBrand())
    }
}