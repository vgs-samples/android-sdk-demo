package com.vgscollect.androiddemo.samples.submit

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.VGSCollectFieldNameMappingPolicy
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.vgscollect.androiddemo.R
import kotlinx.android.synthetic.main.activity_layout.*

class SubmitRequestActivity : AppCompatActivity() {

    private val vgsCollect: VGSCollect by lazy {
        VGSCollect.Builder(this, "<VAULT_ID>").create()
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

        // Set static extra headers (This headers will be send with all requests)
        vgsCollect.setCustomHeaders(
            mapOf(
                "<HEADER_NAME>" to "<HEADER_VALUE>",
                "<HEADER_NAME_2>" to "<HEADER_VALUE_2>"
            )
        )

        // Set static extra data (This headers will be send with all requests)
        // If you add extra data to request, you may want to specify mapping policy.
        // Read about mapping policy https://www.verygoodsecurity.com/docs/vgs-collect/android-sdk/submit-data#nested-json-arrays
        vgsCollect.setCustomData(
            mapOf(
                "<HEADER_NAME>" to "<HEADER_VALUE>",
                "<HEADER_NAME_2>" to "<HEADER_VALUE_2>"
            )
        )

        // Create request
        val request = VGSRequest.VGSRequestBuilder()
            .setPath("<PATH>")
            .setMethod(HTTPMethod.POST)
            /** VGS Collect SDK will ignore data from fields */
            .ignoreFields()
            /** VGS Collect SDK will ignore attached files */
            .ignoreFiles()
            /** Set custom headers that will be send only with this request*/
            .setCustomHeader(
                mapOf(
                    "<HEADER_NAME_3>" to "<HEADER_VALUE_3>",
                    "<HEADER_NAME_4>" to "<HEADER_VALUE_4>"
                )
            )
            /** Set custom data that will be send only with this request */
            .setCustomData(
                mapOf(
                    "data" to "custom_data"
                )
            )
            /**
             * If you add extra data to request, you may want to specify mapping policy.
             * Read about mapping policy https://www.verygoodsecurity.com/docs/vgs-collect/android-sdk/submit-data#nested-json-arrays
             */
            .setFieldNameMappingPolicy(VGSCollectFieldNameMappingPolicy.NESTED_JSON)
            .build()

        // Submit request
        vgsCollect.asyncSubmit(request)
    }
}