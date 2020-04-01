package com.vgscollect.androiddemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.verygoodsecurity.api.cardio.ScanActivity
import com.verygoodsecurity.vgscollect.core.Environment
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), VgsCollectResponseListener, View.OnClickListener {

    companion object {
        const val USER_SCAN_REQUEST_CODE = 0x7
    }

    private lateinit var vgsForm:VGSCollect

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vgsForm = VGSCollect(this, "tntstwggghg", Environment.SANDBOX)

        submitBtn?.setOnClickListener(this)
        attachFileBtn?.setOnClickListener(this)
        scanBtn?.setOnClickListener(this)

        vgsForm.addOnResponseListeners(this)

        vgsForm.addOnFieldStateChangeListener(getOnFieldStateChangeListener())

        vgsForm.bindView(cardNumberField)
        vgsForm.bindView(cardCVCField)
        vgsForm.bindView(cardHolderField)
        vgsForm.bindView(cardExpDateField)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        vgsForm.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        vgsForm.onDestroy()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submitBtn -> submitData()
            R.id.attachFileBtn -> attachFile()
            R.id.scanBtn -> scanData()
        }
    }

    private fun attachFile() {
        vgsForm.getFileProvider().attachFile("attachments.file")
    }

    private fun submitData() {
        progressBar?.visibility = View.VISIBLE
        hideKeyboard(this)
        val customData = generateCustomData()
        val customHeaders = generateCustomHeaders()

        val request: VGSRequest = VGSRequest.VGSRequestBuilder()
            .setMethod(HTTPMethod.POST)
            .setPath("/post")
            .setCustomHeader(customHeaders)
            .setCustomData(customData)
            .build()

        vgsForm.asyncSubmit(request)
    }

    private fun generateCustomHeaders():HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers["some-headers"] = "custom-header"

        return headers
    }

    private fun generateCustomData():HashMap<String, Any> {
        val customData = HashMap<String, Any>()

        val account = HashMap<String, Any>()
        account["id"] = 8485747
        account["username"] = "Grisha"

        customData["account"] = account

        return customData
    }

    private fun scanData() {
        val intent = Intent(this, ScanActivity::class.java)

        val scanSettings = hashMapOf<String?, Int>().apply {
            this[cardNumberField?.getFieldName()] = ScanActivity.CARD_NUMBER
            this[cardCVCField?.getFieldName()] = ScanActivity.CARD_CVC
            this[cardHolderField?.getFieldName()] = ScanActivity.CARD_HOLDER
            this[cardExpDateField?.getFieldName()] = ScanActivity.CARD_EXP_DATE
        }

        intent.putExtra(ScanActivity.SCAN_CONFIGURATION, scanSettings)

        startActivityForResult(intent, USER_SCAN_REQUEST_CODE)
    }

    private fun getOnFieldStateChangeListener(): OnFieldStateChangeListener {
        return object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {
                titleHeader?.text = "STATE:"
                val states = vgsForm.getAllStates()
                val builder = StringBuilder()
                states.forEach {
                    builder.append(it.fieldName).append("\n")
                        .append("   hasFocus: ").append(it.hasFocus).append("\n")
                        .append("   isValid: ").append(it.isValid).append("\n")
                        .append("   isEmpty: ").append(it.isEmpty).append("\n")
                        .append("   isRequired: ").append(it.isRequired).append("\n")
                    if (it is FieldState.CardNumberState) {
                        builder.append("    type: ").append(it.cardBrand).append("\n")
                            .append("       end: ").append(it.last).append("\n")
                            .append("       bin: ").append(it.bin).append("\n")
                            .append(it.number).append("\n")
                    }

                    builder.append("\n")
                }
                responseView?.text = builder.toString()
            }
        }
    }

    override fun onResponse(response: VGSResponse?) {
        progressBar?.visibility = View.INVISIBLE

        titleHeader?.text = "RESPONSE:"
        when (response) {
            is VGSResponse.SuccessResponse -> {
                Log.e("test", "${response.rawResponse}")

                val str = StringBuilder("CODE: ")
                    .append(response.code.toString()).toString()
                responseView.text = str
            }
            is VGSResponse.ErrorResponse -> responseView.text =
                "CODE: ${response.errorCode} \n\n ${response.localizeMessage}"
        }
    }
}
