package com.vgscollect.androiddemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import com.verygoodsecurity.api.cardio.ScanActivity
import com.verygoodsecurity.vgscollect.core.Environment
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.view.card.CustomCardBrand
import com.verygoodsecurity.vgscollect.widget.VGSTextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity: AppCompatActivity(), VgsCollectResponseListener, View.OnClickListener  {

    companion object {
        const val USER_SCAN_REQUEST_CODE = 0x7

        private const val VAULT_ID = "tntstwggghg"
        private val ENVIRONMENT = Environment.SANDBOX
        private const val PATH = "/post"
    }

    private lateinit var vgsForm: VGSCollect
    private val maskAdapter = MaskAdapter()
    private lateinit var iconAdapter:IconAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vgsForm = VGSCollect(this, VAULT_ID, ENVIRONMENT)
        iconAdapter = IconAdapter(this)

        submitBtn?.setOnClickListener(this)
        attachBtn?.setOnClickListener(this)

        vgsForm.addOnResponseListeners(this)
        vgsForm.addOnFieldStateChangeListener(getOnFieldStateChangeListener())

        setupCardNumberField()
        setupCVCField()
        setupCardHolderField()
        setupCardExpDateField()

        attachStaticData()
    }

    private fun setupCardExpDateField() {
        vgsForm.bindView(cardExpDateField)
        cardExpDateField?.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {
                Log.e("card_exp", "$state \n\n ${cardExpDateField.getState()} ")
                if(!state.isEmpty && !state.isValid && !state.hasFocus) {
                    cardExpDateFieldLay?.setError("fill it please")
                } else {
                    cardExpDateFieldLay?.setError(null)
                }
            }
        })
    }

    private fun setupCardHolderField() {
        vgsForm.bindView(cardHolderField)
        cardHolderField?.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {
                Log.e("card_holder", "$state \n\n ${cardHolderField.getState()} ")
                if(!state.isEmpty && !state.isValid && !state.hasFocus) {
                    cardHolderFieldLay?.setError("fill it please")
                } else {
                    cardHolderFieldLay?.setError(null)
                }
            }
        })
    }

    private fun setupCVCField() {
        vgsForm.bindView(cardCVCField)
        cardCVCField?.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {
                Log.e("card_cvc", "$state \n\n ${cardCVCField.getState()} ")
                if(!state.isEmpty && !state.isValid && !state.hasFocus) {
                    cardCVCFieldLay?.setError("fill it please")
                } else {
                    cardCVCFieldLay?.setError(null)
                }
            }
        })
    }

    private fun setupCardNumberField() {
        vgsForm.bindView(cardNumberField)
        setupAdapters()
        addCustomBrand()


        cardNumberField?.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {
                Log.e("card_number", "$state \n\n ${cardNumberField.getState()} ")
            }
        })
    }

    private fun addCustomBrand() {
        val c = CustomCardBrand(
            "^47712",
            "TEst Visa",
            R.drawable.ic_visa_dark,
            "### ## ####### ####"
        )
        cardNumberField?.addCardBrand(c)
    }

    private fun setupAdapters() {
        cardNumberField?.setCardMaskAdapter(maskAdapter)
        cardNumberField?.setCardIconAdapter(iconAdapter)
    }

    private fun attachStaticData() {
        val staticData = mutableMapOf<String, String>()
        staticData["static_data"] = "static custom data"
        vgsForm.setCustomData(staticData)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.scan_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == R.id.scan_card) {
            scanCard()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun scanCard() {
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
                Log.e("vgs_collect_state", "$state ")
                when(state) {
                    is FieldState.CardNumberState -> handleCardNumberState(state)
                    is FieldState.CardExpirationDateState -> showErrorIfNotValidInput(cardExpDateFieldLay, state)
                    is FieldState.CardHolderNameState -> showErrorIfNotValidInput(cardHolderFieldLay, state)
                    is FieldState.CVCState -> showErrorIfNotValidInput(cardCVCFieldLay, state)
                }
                refreshAllStates()
            }
        }
    }

    private fun showErrorIfNotValidInput(
        layout: VGSTextInputLayout?,
        state: FieldState
    ) {
        if(!state.isValid && !state.hasFocus && !state.isEmpty) {
            layout?.setError(getString(R.string.error_is_not_valid))
        } else {
            layout?.setError(null)
        }
    }

    private fun handleCardNumberState(state: FieldState.CardNumberState) {
        showErrorIfNotValidInput(cardNumberFieldLay, state)

        previewCardNumber?.text = state.number
        if(state.cardBrand == CardType.VISA.name) {
            previewCardBrand?.setImageResource(R.drawable.ic_custom_visa)
        } else {
            previewCardBrand?.setImageResource(state.drawableBrandResId)
        }
    }

    private fun refreshAllStates() {
        val states = vgsForm.getAllStates()
        val builder = StringBuilder()
        states.forEach {
            builder.append(it.toString()).append("\n\n")
        }
        stateContainerView?.text = builder.toString()
    }

    override fun onDestroy() {
        vgsForm.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        vgsForm.onActivityResult(requestCode, resultCode, data)
        checkAttachedFiles()
    }

    private fun checkAttachedFiles() {
        if(vgsForm.getFileProvider().getAttachedFiles().isEmpty()) {
            attachBtn?.text = getString(R.string.collect_activity_attach_btn)
        } else {
            attachBtn?.text = getString(R.string.collect_activity_detach_btn)
        }
    }

    override fun onResponse(response: VGSResponse?) {
        setEnabledResponseHeader(true)
        setStateLoading(false)

        when (response) {
            is VGSResponse.SuccessResponse -> {
                responseContainerView.text = response.toString()
            }
            is VGSResponse.ErrorResponse -> responseContainerView.text = response.toString()
        }
    }

    private fun setStateLoading(state:Boolean) {
        if(state) {
            progressBar?.visibility = View.VISIBLE
            submitBtn?.isEnabled = false
            attachBtn?.isEnabled = false
        } else {
            progressBar?.visibility = View.INVISIBLE
            submitBtn?.isEnabled = true
            attachBtn?.isEnabled = true
        }
    }

    private fun setEnabledResponseHeader(isEnabled:Boolean) {
        if(isEnabled) {
            attachBtn.setTextColor(ContextCompat.getColor(this, R.color.state_active))
            responseTitleView.setTextColor(ContextCompat.getColor(this, R.color.state_active))
        } else {
            responseContainerView.text = ""
            attachBtn.setTextColor(ContextCompat.getColor(this, R.color.state_unactive))
            responseTitleView.setTextColor(ContextCompat.getColor(this, R.color.state_unactive))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submitBtn -> submitData()
            R.id.attachBtn -> attachFile()
        }
    }

    private fun submitData() {
        setEnabledResponseHeader(false)
        setStateLoading(true)

        val customData = HashMap<String, Any>()
        customData["nickname"] = "Taras"

        val headers = HashMap<String, String>()
        headers["some-headers"] = "dynamic-header"

        val request: VGSRequest = VGSRequest.VGSRequestBuilder()
            .setMethod(HTTPMethod.POST)
            .setPath(PATH)
            .setCustomHeader(headers)
            .setCustomData(customData)
            .build()

        vgsForm.asyncSubmit(request)
    }

    private fun attachFile() {
        if(vgsForm.getFileProvider().getAttachedFiles().isEmpty()) {
            vgsForm.getFileProvider().attachFile("attachments.file")
        } else {
            vgsForm.getFileProvider().detachAll()
        }
        checkAttachedFiles()
    }
}