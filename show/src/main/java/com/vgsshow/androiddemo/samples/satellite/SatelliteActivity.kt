package com.vgsshow.androiddemo.samples.satellite

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.verygoodsecurity.vgsshow.VGSShow
import com.verygoodsecurity.vgsshow.core.listener.VGSOnResponseListener
import com.verygoodsecurity.vgsshow.core.network.client.VGSHttpMethod
import com.verygoodsecurity.vgsshow.core.network.model.VGSRequest
import com.verygoodsecurity.vgsshow.core.network.model.VGSResponse
import com.verygoodsecurity.vgsshow.widget.VGSTextView
import com.vgsshow.androiddemo.BuildConfig
import com.vgsshow.androiddemo.R
import kotlinx.android.synthetic.main.activity_layout.*

class SatelliteActivity: AppCompatActivity() {

    /**
     * Read VGS Show SDK integration with VGS-Satellite
     * <a href="https://www.verygoodsecurity.com/docs/vgs-show/android-sdk/vgs-satellite-integration/">guide</a>.
     */
    private val vgsShow = VGSShow.Builder(this, BuildConfig.VAULT_ID)
        .setHostname("<HOST>") // Set VGS-Satellite host, if you run app on AVD it should be 10.0.2.2(localhost alias), read documentation for more examples.
        .setPort(9098) // Set VGS-Satellite port, 9098 is default VGS-Satellite reverse proxy port, set correct port if you have edited your VGS-Satellite configuration.
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        // Setup view
        val tvCardNumber = VGSTextView(this)
        tvCardNumber.setContentPath("<CONTENT_PATH>")
        tvCardNumber.setHint("Fetching card number...")
        tvCardNumber.setHintTextColor(ContextCompat.getColor(this, android.R.color.black))
        rootView.addView(tvCardNumber)

        // Subscribe view
        vgsShow.subscribe(tvCardNumber)

        // Handle response
        vgsShow.addOnResponseListener(object : VGSOnResponseListener {

            override fun onResponse(response: VGSResponse) {
                Log.d(this@SatelliteActivity::class.java.simpleName, response.toString())
            }
        })

        // Make request
        vgsShow.requestAsync(
            VGSRequest.Builder("<PATH>", VGSHttpMethod.POST)
                .body(mapOf(/** <PAYLOAD> */))
                .build()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        vgsShow.onDestroy()
    }
}