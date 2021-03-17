package com.vgscollect.androiddemo.usecase.payments.checkout.collect

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscollect.core.Environment
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse
import com.vgscollect.androiddemo.R
import kotlinx.android.synthetic.main.activity_collect_components.*
import kotlinx.android.synthetic.main.activity_main.*

class CollectCheckoutFormActivity: AppCompatActivity() {

    private val vgsForm: VGSCollect by lazy {
        VGSCollect.Builder(this, "tntpszqgikn")
            .setEnvironment(Environment.SANDBOX)
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect_components)
        mbAddCard?.setOnClickListener { submitData() }
        setupCollect()
    }

    private fun submitData() {
        val request: VGSRequest = VGSRequest.VGSRequestBuilder()
            .setMethod(HTTPMethod.POST)
            .setPath("/post")
            .build()
        vgsForm.asyncSubmit(request)
    }

    private fun setupCollect() {
        vgsForm.bindView(etCardNumber)
        vgsForm.bindView(etCardHolderName)
        vgsForm.bindView(etDate)
        vgsForm.bindView(etCVC)
        vgsForm.addOnResponseListeners(object : VgsCollectResponseListener {
            override fun onResponse(response: VGSResponse?) {
                when(response?.code) {
                    null -> return
                    200 -> Toast.makeText(this@CollectCheckoutFormActivity, "Card added!", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this@CollectCheckoutFormActivity, " $response", Toast.LENGTH_SHORT).show()
                }

            }
        })
    }
}