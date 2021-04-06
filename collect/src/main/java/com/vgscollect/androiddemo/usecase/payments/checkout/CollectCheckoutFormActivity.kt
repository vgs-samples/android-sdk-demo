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
import com.vgscollect.androiddemo.R
import kotlinx.android.synthetic.main.activity_payments_checkout_collect_components.*

class CollectCheckoutFormActivity : AppCompatActivity() {

    private val vgsForm: VGSCollect by lazy {
        VGSCollect.Builder(this, "tntpszqgikn")
            .setEnvironment(Environment.SANDBOX)
            .create()
    }

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