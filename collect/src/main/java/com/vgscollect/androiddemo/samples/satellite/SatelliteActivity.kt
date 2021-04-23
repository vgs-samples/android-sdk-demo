package com.vgscollect.androiddemo.samples.satellite

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

class SatelliteActivity : AppCompatActivity() {

    private lateinit var vgsCollect: VGSCollect

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        // Init collect, read VGS Collect SDK integration with VGS-Satellite https://www.verygoodsecurity.com/docs/vgs-collect/android-sdk/vgs-satellite-integration/
        vgsCollect = VGSCollect.Builder(this, "<vault_id>")
            .setEnvironment("<environment>")
            .setHostname("<host>") // Set VGS-Satellite host, if you run app on AVD it should be 10.0.2.2(localhost alias), read documentation for more examples, don't forget to add network security config
            .setPort(9098) // Set VGS-Satellite port, 9098 is default VGS-Satellite reverse proxy port, set correct port if you have edited your VGS-Satellite configuration.
            .create()

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

        // Handle response
        vgsCollect.addOnResponseListeners(object : VgsCollectResponseListener {

            override fun onResponse(response: VGSResponse?) {
                Log.d(this@SatelliteActivity::class.java.simpleName, response.toString())
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