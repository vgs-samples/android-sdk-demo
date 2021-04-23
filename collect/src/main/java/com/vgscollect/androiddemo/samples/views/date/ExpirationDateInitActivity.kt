package com.vgscollect.androiddemo.samples.views.date

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscollect.view.core.serializers.VGSExpDateSeparateSerializer
import com.verygoodsecurity.vgscollect.view.date.DatePickerMode
import com.verygoodsecurity.vgscollect.widget.ExpirationDateEditText
import com.vgscollect.androiddemo.R
import kotlinx.android.synthetic.main.activity_layout.*

class ExpirationDateInitActivity : AppCompatActivity() {

    private val vgsCollect: VGSCollect by lazy {
        VGSCollect.Builder(this, "<VAULT_ID>").create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        // Create expiration date view
        val vgsEtExpirationDate = ExpirationDateEditText(this).apply {
            setFieldName("<FIELD_NAME>")
            setHint("Date")
            setPadding(resources.getDimensionPixelSize(R.dimen.padding))
        }
        rootView.addView(vgsEtExpirationDate)

        // Subscribe view
        vgsCollect.bindView(vgsEtExpirationDate)

        // Set date picker mode CALENDAR, SPINNER, INPUT or DEFAULT
        vgsEtExpirationDate.setDatePickerMode(DatePickerMode.INPUT)

        // Set date serializer. Allows VGS Collect SDK to send selected month and year as separate fields. Supported from VGS Collect SDK v1.6.4
        vgsEtExpirationDate.setSerializer(
            VGSExpDateSeparateSerializer(
                "<MONTH_FIELD_NAME>",
                "<YEAR_FIELD_NAME>"
            )
        )

        // Set date output regex. Date will be send to beck-end in specified format.
        vgsEtExpirationDate.setOutputRegex("MM/yyyy")

        // Add field state change listener
        vgsEtExpirationDate.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {

            override fun onStateChange(state: FieldState) {
                Log.d(ExpirationDateInitActivity::class.simpleName, state.toString())
            }
        })
    }
}