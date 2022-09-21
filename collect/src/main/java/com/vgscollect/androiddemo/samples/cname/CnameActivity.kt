package com.vgscollect.androiddemo.samples.cname

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.vgscollect.androiddemo.R

class CnameActivity : AppCompatActivity() {

    private lateinit var vgsCollect: VGSCollect

    private val rootView: FrameLayout by lazy { findViewById(R.id.rootView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        /*
         * Init collect.
         * Configure custom host name in [VGS dashboard](https://dashboard.verygoodsecurity.com/) before using it.
         */
        vgsCollect = VGSCollect.Builder(this, "<vault_id>")
            .setEnvironment("<environment>")
            .setHostname("<host>") // Set custom hostname, for example: https://example.com
            .create()

        /* Setup view */
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