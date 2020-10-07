package com.vgscollect.androiddemo.samples.paymentcardnumber

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscollect.view.card.BrandParams
import com.verygoodsecurity.vgscollect.view.card.CardBrand
import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.view.card.formatter.CardMaskAdapter
import com.verygoodsecurity.vgscollect.view.card.icon.CardIconAdapter
import com.verygoodsecurity.vgscollect.view.card.validation.payment.ChecksumAlgorithm
import com.verygoodsecurity.vgscollect.view.card.validation.rules.PaymentCardNumberRule
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.vgscollect.androiddemo.R
import kotlinx.android.synthetic.main.activity_payment_card_number_field.*

class PaymentCardNumberActivity : AppCompatActivity() {

    private lateinit var cardNumber1 : VGSCardNumberEditText
    private lateinit var cardNumber2 : VGSCardNumberEditText

    private lateinit var vgsForm: VGSCollect

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_card_number_field)

        // Initialize VGSCollect
        vgsForm = VGSCollect(this, "<vault_id>", "<environment>")

        cardNumber1 = cardNumberFromLayout

        createFieldProgrammatically()

        bindFieldToVGSCollect()
    }

    // Initialize SSN field programmatically.
    fun createFieldProgrammatically() {
        cardNumber2 = VGSCardNumberEditText(this)
        parentLayout.addView(cardNumber2)
    }

    // Bind fields to the VGSCollect.
    // After this action your field become available for VGSCollect. Only now you can send user data on Proxy server.
    private fun bindFieldToVGSCollect() {
        vgsForm.bindView(cardNumber1)
        vgsForm.bindView(cardNumber2)
    }

    //Add custom brand for detection and validation.
    fun addCustomBrand() {
        val params = BrandParams(
            "",
            ChecksumAlgorithm.LUHN,
            arrayOf(14,16,19)
        )

        val brand = CardBrand(
            "<regex_to_detect_the_brand>",
            "VGS Brand",
            R.mipmap.ic_launcher,
            params
        )
        cardNumber2.addCardBrand(brand)
    }

    //Update gravity of the card icon gravity
    fun updateCardBrandIconGravity() {
        cardNumber2.setCardBrandIconGravity(Gravity.END)
        cardNumber2.setCardBrandIconGravity(Gravity.START)
        cardNumber2.setCardBrandIconGravity(Gravity.NO_GRAVITY)
    }

    //set custom mask for card brand
    fun handleCustomMasks() {
        cardNumber2.setCardMaskAdapter(object : CardMaskAdapter() {
            override fun getMask(
                cardType: CardType,
                name: String,
                bin: String,
                mask: String
            ): String {
                return when(cardType) {
                    CardType.VISA_ELECTRON ->  "###### ###### ####"
                    CardType.UNKNOWN ->  "#### #### #### ####"
                    else -> super.getMask(cardType, name, bin, mask)
                }
            }
        })
    }

    //handle custom icon for card brand
    fun handleCustomIcon() {
        cardNumber2.setCardIconAdapter(object : CardIconAdapter(this) {
            override fun getIcon(cardType: CardType, name: String?, resId: Int, r: Rect): Drawable {
                return when(cardType) {
                    CardType.VISA_ELECTRON -> getDrawable(R.drawable.ic_visa_light)
                    CardType.VISA ->  getDrawable(R.drawable.ic_visa_dark)
                    else -> super.getIcon(cardType, name, resId, r)
                }
            }
        })
    }

    //add custom validation rules
    fun addValidationRules() {
        val rule : PaymentCardNumberRule = PaymentCardNumberRule.ValidationBuilder()
            .setAlgorithm(ChecksumAlgorithm.LUHN)
            .setAllowableMaxLength(19)
            .setAllowableMinLength(14)
            .build()

        cardNumber2.addRule(rule)
    }


    fun manageField() {
        // field allows you to retrieve current its state.
        val state: FieldState.CardNumberState? = cardNumber2.getState()

        // Allows you to track runtime field state.
        cardNumber2.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {

            }
        })

        //Set <fieldName> programmatically
        cardNumber2.setFieldName("<fieldName>")

        cardNumber2.setFieldName("<fieldName>")

        cardNumber2.setDivider('-')

    }

}