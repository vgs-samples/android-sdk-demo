package com.vgsshow.androiddemo.samples.extra

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.verygoodsecurity.vgsshow.VGSShow
import com.verygoodsecurity.vgsshow.core.listener.VGSOnResponseListener
import com.verygoodsecurity.vgsshow.core.network.client.VGSHttpMethod
import com.verygoodsecurity.vgsshow.core.network.model.VGSRequest
import com.verygoodsecurity.vgsshow.core.network.model.VGSResponse
import com.verygoodsecurity.vgsshow.widget.VGSTextView
import com.vgsshow.androiddemo.R

class ExtraHeadersAndDataActivity : AppCompatActivity() {

    private val vgsShow = VGSShow.Builder(this, "<VAULT_ID>").build()

    private val rootView: FrameLayout by lazy { findViewById(R.id.rootView) }

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
                Log.d(this@ExtraHeadersAndDataActivity::class.java.simpleName, response.toString())
            }
        })

        // Set static extra headers (This headers will be send with all requests)
        vgsShow.setCustomHeader("<HEADER_NAME>", "<HEADER_VALUE>")
        vgsShow.setCustomHeader("<HEADER_NAME_2>", "<HEADER_VALUE>")

        // Make request
        vgsShow.requestAsync(
            VGSRequest.Builder("<PATH>", VGSHttpMethod.POST)
                .headers(mapOf("<HEADER_NAME>" to "<HEADER_VALUE>")) // Set dynamic extra headers (This headers will be send only with this request)
                .body(mapOf(
                    "<CONTENT_PATH>" to "<ALIAS>",
                    "<NESTED_EXTRA_DATA_OBJECT>" to mapOf(
                        "<SOME_EXTRA_DATA_KEY>" to "<SOME_EXTRA_DATA_VALUE>"
                    )
                ))
                .build()
        )
    }
}