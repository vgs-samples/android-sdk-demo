package com.vgscollect.androiddemo.usecase.payments.checkout

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscollect.core.Environment
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscollect.view.InputFieldView
import com.verygoodsecurity.vgscollect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscollect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscollect.widget.PersonNameEditText
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.vgscollect.androiddemo.R

class CollectCheckoutFormActivity : AppCompatActivity() {

    private val vgsForm: VGSCollect by lazy {
        VGSCollect.Builder(this, "tntpszqgikn")
            .setEnvironment(Environment.SANDBOX)
            .create()
    }

    private val etCardNumber: VGSCardNumberEditText by lazy { findViewById(R.id.etCardNumber) }
    private val etCardHolderName: PersonNameEditText by lazy { findViewById(R.id.etCardHolderName) }
    private val etDate: ExpirationDateEditText by lazy { findViewById(R.id.etDate) }
    private val etCVC: CardVerificationCodeEditText by lazy { findViewById(R.id.etCVC) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments_checkout_collect_components)

        vgsForm.bindView(etCardNumber, etCardHolderName, etDate, etCVC)
        vgsForm.addOnResponseListeners(object : VgsCollectResponseListener {
            override fun onResponse(response: VGSResponse?) {
                Log.d(InputFieldView::class.simpleName.toString(), response.toString())
            }
        })
    }

    fun submitData(v: View) {
        vgsForm.asyncSubmit("/post", HTTPMethod.POST)
    }
}