package com.vgscollect.androiddemo.samples.cname

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.vgscollect.androiddemo.R
import kotlinx.android.synthetic.main.activity_layout.*

class CnameActivity : AppCompatActivity() {

    /**
     * Configure custom host name in <a href="https://dashboard.verygoodsecurity.com/">VGS dashboard</a> before using it.
     */
    private val vgsCollect: VGSCollect by lazy {
        VGSCollect.Builder(this, "<VAULT_ID>")
            .setHostname("<HOST_NAME>") // Set custom hostname, for example: https://example.com
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        // Setup view
        val vgsEtCardNumber = VGSCardNumberEditText(this).apply {
            setFieldName("<FIELD_NAME>")
            setHint("Card number")
            setDivider('-')
            setCardBrandIconGravity(Gravity.END)
            setPadding(resources.getDimensionPixelSize(R.dimen.padding))
        }
        rootView.addView(vgsEtCardNumber)

        // Subscribe view
        vgsCollect.bindView(vgsEtCardNumber)

        // Handle response
        vgsCollect.addOnResponseListeners(object : VgsCollectResponseListener {

            override fun onResponse(response: VGSResponse?) {
                Log.d(this@CnameActivity::class.java.simpleName, response.toString())
            }
        })

        // Make request
        vgsCollect.asyncSubmit(
            VGSRequest.VGSRequestBuilder()
                .setPath("<PATH>")
                .setMethod(HTTPMethod.POST)
                .build()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        vgsCollect.onDestroy()
    }
}