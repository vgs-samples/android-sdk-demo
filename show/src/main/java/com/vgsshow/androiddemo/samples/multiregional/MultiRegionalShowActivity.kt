package com.vgsshow.androiddemo.samples.multiregional

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.verygoodsecurity.vgsshow.VGSShow
import com.verygoodsecurity.vgsshow.core.VGSEnvironment
import com.verygoodsecurity.vgsshow.core.listener.VGSOnResponseListener
import com.verygoodsecurity.vgsshow.core.network.client.VGSHttpMethod
import com.verygoodsecurity.vgsshow.core.network.model.VGSRequest
import com.verygoodsecurity.vgsshow.core.network.model.VGSResponse
import com.verygoodsecurity.vgsshow.widget.VGSTextView
import com.vgsshow.androiddemo.BuildConfig
import com.vgsshow.androiddemo.R
import kotlinx.android.synthetic.main.activity_basic_usage.*

class MultiRegionalShowActivity : AppCompatActivity() {

    private val vgsShow: VGSShow = configureViaConstructor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_usage)

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
                Log.d(this@MultiRegionalShowActivity::class.java.simpleName, response.toString())
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

    /**
     * Configure VGSShow instance via constructor with sandbox environment and 'eu' region.
     */
    private fun configureViaConstructor() =
        VGSShow(this, BuildConfig.VAULT_ID, VGSEnvironment.Sandbox("eu"))

    /**
     * Configure VGSShow instance via constructor with sandbox environment and 'eu' region.
     */
    private fun configureViaConstructorUsingString() =
        VGSShow(this, BuildConfig.VAULT_ID, "sandbox-eu")

    /**
     * Configure VGSShow instance via builder with sandbox environment and 'eu' region.
     */
    private fun configureViaBuilder() = VGSShow.Builder(this, BuildConfig.VAULT_ID)
        .setEnvironment(VGSEnvironment.Sandbox("eu"))
        .build()
}