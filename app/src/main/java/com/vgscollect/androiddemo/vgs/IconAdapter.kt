package com.vgscollect.androiddemo.vgs

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.view.card.icon.CardIconAdapter
import com.vgscollect.androiddemo.R

class IconAdapter(c:Context) : CardIconAdapter(c) {
    override fun getIcon(cardType: CardType, name: String?, resId: Int, r: Rect): Drawable {
        return if(cardType == CardType.VISA) {
            getDrawable(R.drawable.ic_visa_light)
        } else {
            super.getIcon(cardType, name, resId, r)
        }
    }
}