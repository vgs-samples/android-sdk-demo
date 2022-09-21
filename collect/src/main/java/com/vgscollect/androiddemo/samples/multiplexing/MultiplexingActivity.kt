package com.vgscollect.androiddemo.samples.multiplexing

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscollect.VGSCollectLogger
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscollect.view.InputFieldView
import com.verygoodsecurity.vgscollect.view.core.serializers.VGSExpDateSeparateSerializer
import com.verygoodsecurity.vgscollect.widget.*
import com.vgscollect.androiddemo.R

class MultiplexingActivity : AppCompatActivity(), VgsCollectResponseListener,
    InputFieldView.OnTextChangedListener {

    private val vgsCollect: VGSCollect by lazy {
        VGSCollect.Builder(this, "<VAULT_ID>").create().apply {
            addOnResponseListeners(this@MultiplexingActivity)
        }
    }

    private val progressBar: ProgressBar by lazy { findViewById(R.id.progressBar) }
    private val tilFirstName: VGSTextInputLayout by lazy { findViewById(R.id.tilFirstName) }
    private val tilLastName: VGSTextInputLayout by lazy { findViewById(R.id.tilLastName) }
    private val tilCardNumber: VGSTextInputLayout by lazy { findViewById(R.id.tilCardNumber) }
    private val tilExpirationDate: VGSTextInputLayout by lazy { findViewById(R.id.tilExpirationDate) }
    private val tilCVC: VGSTextInputLayout by lazy { findViewById(R.id.tilCVC) }
    private val etCardNumber: VGSCardNumberEditText by lazy { findViewById(R.id.etCardNumber) }
    private val etFirstName: PersonNameEditText by lazy { findViewById(R.id.etFirstName) }
    private val etLastName: PersonNameEditText by lazy { findViewById(R.id.etLastName) }
    private val etExpirationDate: ExpirationDateEditText by lazy { findViewById(R.id.etExpirationDate) }
    private val etCVC: CardVerificationCodeEditText by lazy { findViewById(R.id.etCVC) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplexing)
        VGSCollectLogger.logLevel = VGSCollectLogger.Level.DEBUG
        initView()
    }

    override fun onResponse(response: VGSResponse?) {
        Log.d(MultiplexingActivity::class.java.simpleName, response.toString())
        progressBar.visibility = View.GONE
    }

    override fun onTextChange(view: InputFieldView, isEmpty: Boolean) {
        when (view.id) {
            R.id.etFirstName -> tilFirstName.setError(null)
            R.id.etLastName -> tilLastName.setError(null)
            R.id.etCardNumber -> tilCardNumber.setError(null)
            R.id.etExpirationDate -> tilExpirationDate.setError(null)
            R.id.etCVC -> tilCVC.setError(null)
        }
    }

    fun submit(@Suppress("UNUSED_PARAMETER") v: View) {
        validateFieldsAndRun {
            progressBar.visibility = View.VISIBLE
            vgsCollect.asyncSubmit(
                VGSRequest.VGSRequestBuilder()
                    .setPath("/api/v1/financial_instruments")
                    .setCustomData(
                        mutableMapOf(
                            "data" to mutableMapOf(
                                "type" to "financial_instruments",
                                "attributes" to mutableMapOf(
                                    "instrument_type" to "card"
                                )
                            )
                        )
                    )
                    .build()
            )
        }
    }

    private fun initView() {
        // Set expiration date serializer which will send month and year separately
        etExpirationDate.setSerializer(
            VGSExpDateSeparateSerializer(
                "data.attributes.details.month",
                "data.attributes.details.year"
            )
        )

        // Set view state change listener
        etFirstName.addOnTextChangeListener(this)
        etLastName.addOnTextChangeListener(this)
        etCardNumber.addOnTextChangeListener(this)
        etExpirationDate.addOnTextChangeListener(this)
        etCVC.addOnTextChangeListener(this)

        // Bind VGS view to VGSCollect
        vgsCollect.bindView(etFirstName, etLastName, etCardNumber, etExpirationDate, etCVC)
    }

    private inline fun validateFieldsAndRun(action: () -> Unit) {
        var shouldRun = true
        if (etFirstName.getState()?.isValid == false) {
            tilFirstName.setError("Please, enter valid name!")
            shouldRun = false
        }
        if (etLastName.getState()?.isValid == false) {
            tilLastName.setError("Please, enter valid last name!")
            shouldRun = false
        }
        if (etCardNumber.getState()?.isValid == false) {
            tilCardNumber.setError("Please, enter valid card number!")
            shouldRun = false
        }
        if (etExpirationDate.getState()?.isValid == false) {
            tilExpirationDate.setError("Please, enter valid expiration date!")
            shouldRun = false
        }
        if (etCVC.getState()?.isValid == false) {
            tilCVC.setError("Please, enter valid cvc!")
            shouldRun = false
        }
        if (shouldRun) action.invoke()
    }
}