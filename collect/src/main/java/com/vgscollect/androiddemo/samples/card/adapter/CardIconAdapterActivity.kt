package com.vgscollect.androiddemo.samples.card.adapter

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.view.card.icon.CardIconAdapter
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.vgscollect.androiddemo.R

class CardIconAdapterActivity : AppCompatActivity() {

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

        // Add card icon adapter and override VISA card icon
        vgsEtCardNumber.setCardIconAdapter(object : CardIconAdapter(this) {

            private val iconBounds: Rect = Rect(
                0,
                0,
                resources.getDimensionPixelSize(R.dimen.icon_size),
                resources.getDimensionPixelSize(R.dimen.icon_size)
            )

            override fun getIcon(cardType: CardType, name: String?, resId: Int, r: Rect): Drawable {
                if (cardType == CardType.VISA) {
                    return getDrawable(R.drawable.ic_custom_visa).apply { bounds = iconBounds }
                }
                return super.getIcon(cardType, name, resId, r)
            }
        })
    }
}