package com.vgscollect.androiddemo.usecase.payments.demo

import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.view.card.formatter.CardMaskAdapter

class MaskAdapter: CardMaskAdapter() {
    override fun getMask(cardType: CardType, name: String, bin: String, mask: String): String {
        return if(cardType == CardType.MAESTRO && bin == "675964") {
            "###### ###### ####"
        } else {
            super.getMask(cardType, name, bin, mask)
        }
    }
}