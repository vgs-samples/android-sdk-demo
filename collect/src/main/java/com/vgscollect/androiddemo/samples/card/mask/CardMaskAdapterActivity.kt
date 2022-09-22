package com.vgscollect.androiddemo.samples.card.mask

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.view.card.formatter.CardMaskAdapter
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.vgscollect.androiddemo.R

class CardMaskAdapterActivity : AppCompatActivity() {

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

        // Add card mask adapter and override VISA mask
        vgsEtCardNumber.setCardMaskAdapter(object : CardMaskAdapter() {

            override fun getMask(
                cardType: CardType,
                name: String,
                bin: String,
                mask: String
            ): String = when (cardType) {
                CardType.VISA_ELECTRON -> "###### ###### ####"
                else -> super.getMask(cardType, name, bin, mask)
            }
        })
    }
}