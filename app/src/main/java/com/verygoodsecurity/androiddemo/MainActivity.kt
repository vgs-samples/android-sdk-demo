package com.verygoodsecurity.androiddemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.verygoodsecurity.api.bouncer.ScanActivity
import com.verygoodsecurity.vgscollect.VGSCollectLogger
import com.verygoodsecurity.vgscollect.core.Environment
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscollect.view.card.BrandParams
import com.verygoodsecurity.vgscollect.view.card.CardBrand
import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.view.card.validation.payment.ChecksumAlgorithm
import com.verygoodsecurity.vgscollect.view.card.validation.rules.PaymentCardNumberRule
import com.verygoodsecurity.vgscollect.view.card.validation.rules.PersonNameRule
import com.verygoodsecurity.vgscollect.view.core.serializers.VGSExpDateSeparateSerializer
import com.verygoodsecurity.vgscollect.widget.*
import com.verygoodsecurity.vgsshow.VGSShow
import com.verygoodsecurity.vgsshow.core.VGSEnvironment
import com.verygoodsecurity.vgsshow.core.listener.VGSOnResponseListener
import com.verygoodsecurity.vgsshow.core.logs.VGSShowLogger
import com.verygoodsecurity.vgsshow.core.network.client.VGSHttpMethod
import com.verygoodsecurity.vgsshow.widget.VGSTextView
import com.verygoodsecurity.vgsshow.widget.view.textview.model.VGSTextRange
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.attachBtn
import kotlinx.android.synthetic.main.layout_show_reveal_card.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val USER_SCAN_REQUEST_CODE = 0x7

        private const val VAULT_ID = "tntpszqgikn"
        private val ENVIRONMENT = Environment.SANDBOX
        private const val PATH = "/post"
    }

    private val vgsForm: VGSCollect by lazy {
        VGSCollect.Builder(this, VAULT_ID)
            .setEnvironment(ENVIRONMENT)
            .create()
    }

    private val vgsShow: VGSShow by lazy {
        VGSShow.Builder(this, VAULT_ID)
            .setEnvironment(VGSEnvironment.Sandbox())
            .build()
    }

    private val maskAdapter = MaskAdapter()
    private lateinit var iconAdapter: IconAdapter

    // general controls
    private val dateToken: TextView? by lazy { findViewById(R.id.dateToken) }
    private val numberToken: TextView? by lazy { findViewById(R.id.numberToken) }
    private val previewCardNumber: TextView? by lazy { findViewById(R.id.previewCardNumber) }
    private val previewCardBrand: ImageView? by lazy { findViewById(R.id.previewCardBrand) }


    // Collect SDK controls
    private val cardNumberField: VGSCardNumberEditText? by lazy { findViewById(R.id.cardNumberField) }
    private val cardNumberFieldLay: VGSTextInputLayout? by lazy { findViewById(R.id.cardNumberFieldLay) }

    private val cardHolderField: PersonNameEditText? by lazy { findViewById(R.id.cardHolderField) }
    private val cardHolderFieldLay: VGSTextInputLayout? by lazy { findViewById(R.id.cardHolderFieldLay) }

    private val cardCVCField: CardVerificationCodeEditText? by lazy { findViewById(R.id.cardCVCField) }
    private val cardCVCFieldLay: VGSTextInputLayout? by lazy { findViewById(R.id.cardCVCFieldLay) }

    private val cardExpDateField: ExpirationDateEditText? by lazy { findViewById(R.id.cardExpDateField) }
    private val cardExpDateFieldLay: VGSTextInputLayout? by lazy { findViewById(R.id.cardExpDateFieldLay) }


    //Show SDK controls
    private val revealedNumber: VGSTextView? by lazy { findViewById(R.id.revealedNumber) }

    private var showContentIsHidden = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        submitBtn?.setOnClickListener(this)
        revealBtn?.setOnClickListener(this)
        attachBtn?.setOnClickListener(this)
        passwordIcon?.setOnClickListener(this)

        setupCollect()
        setupShow()
    }

    private fun setupCollect() {
        VGSCollectLogger.isEnabled = true
        VGSCollectLogger.logLevel = VGSCollectLogger.Level.DEBUG

        iconAdapter = IconAdapter(this)

        vgsForm.addOnResponseListeners(object : VgsCollectResponseListener {
            override fun onResponse(response: VGSResponse?) {
                setEnabledResponseHeader(true)
                setStateLoading(false)

                when (response) {
                    is VGSResponse.SuccessResponse -> {
                        responseContainerView.text =
                            "Collect Response Code: \n ${response.successCode}"

                        try {
                            val json = when (response) {
                                is CollectSuccessResponse -> JSONObject(response?.rawResponse)
                                else -> null
                            }

                            parseNumberAlias(json)
                        } catch (e: JSONException) {
                        }
                    }
                    is VGSResponse.ErrorResponse -> responseContainerView.text = response.toString()
                }
            }
        })

        vgsForm.addOnFieldStateChangeListener(getOnFieldStateChangeListener())

        setupCardNumberField()
        setupCVCField()
        setupCardHolderField()
        setupCardExpDateField()

        attachStaticData()
    }

    private fun setupCardExpDateField() {
        vgsForm.bindView(cardExpDateField)

        cardExpDateField?.setSerializer(
            VGSExpDateSeparateSerializer(
                "card_data.personal_data.month",
                "card_data.personal_data.year"
            )
        )

        cardExpDateField?.apply {
            setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
                override fun onStateChange(state: FieldState) {
                    Log.e("card_exp", "$state \n\n ${getState()} ")
                    if (!state.isEmpty && !state.isValid && !state.hasFocus) {
                        cardExpDateFieldLay?.setError("fill it please")
                    } else {
                        cardExpDateFieldLay?.setError(null)
                    }
                }
            })
        }
    }

    private fun setupCardHolderField() {
        val rule: PersonNameRule = PersonNameRule.ValidationBuilder()
            .setAllowableMinLength(3)
            .setAllowableMaxLength(7)
            .build()

        cardHolderField?.apply {
            addRule(rule)

            setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
                override fun onStateChange(state: FieldState) {
                    Log.e("card_holder", "$state \n\n ${getState()} ")
                    if (!state.isEmpty && !state.isValid && !state.hasFocus) {
                        cardHolderFieldLay?.setError("fill it please")
                    } else {
                        cardHolderFieldLay?.setError(null)
                    }
                }
            })
        }


        vgsForm.bindView(cardHolderField)

    }

    private fun setupCVCField() {
        cardCVCField?.apply {
            setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
                override fun onStateChange(state: FieldState) {
                    Log.e("card_cvc", "$state \n\n ${getState()} ")
                    if (!state.isEmpty && !state.isValid && !state.hasFocus) {
                        cardCVCFieldLay?.setError("fill it please")
                    } else {
                        cardCVCFieldLay?.setError(null)
                    }
                }
            })
        }

        vgsForm.bindView(cardCVCField)
    }

    private fun setupCardNumberField() {
        setupAdapters()
        addCustomBrand()
        setupDefaultValidationRules()

        cardNumberField?.apply {
            setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
                override fun onStateChange(state: FieldState) {
                    Log.e("card_number", "$state \n\n ${getState()} ")
                }
            })
        }

        vgsForm.bindView(cardNumberField)
    }

    private fun setupDefaultValidationRules() {
        val rule: PaymentCardNumberRule = PaymentCardNumberRule.ValidationBuilder()
            .setAlgorithm(ChecksumAlgorithm.LUHN)
            .setAllowableNumberLength(arrayOf(15, 16, 19))
            .build()

        cardNumberField?.addRule(rule)
    }

    private fun addCustomBrand() {
        val params = BrandParams(
            "### ### ### ###",
            ChecksumAlgorithm.LUHN,
            arrayOf(4, 10, 12),
            arrayOf(3, 5)
        )
        val c = CardBrand(
            "^47712",
            "Internal Visa",
            R.drawable.ic_visa_dark,
            params
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
        return if (item.itemId == R.id.scan_card) {
            scanCard()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun scanCard() {
        val bndl = with(Bundle()) {
            val scanSettings = hashMapOf<String?, Int>().apply {
                this[cardNumberField?.getFieldName()] = ScanActivity.CARD_NUMBER
                this[cardCVCField?.getFieldName()] = ScanActivity.CARD_CVC
                this[cardHolderField?.getFieldName()] = ScanActivity.CARD_HOLDER
                this[cardExpDateField?.getFieldName()] = ScanActivity.CARD_EXP_DATE
            }
            putSerializable(ScanActivity.SCAN_CONFIGURATION, scanSettings)

            putString(ScanActivity.API_KEY, "<bouncer-api-key>")

            putBoolean(ScanActivity.ENABLE_EXPIRY_EXTRACTION, false)
            putBoolean(ScanActivity.ENABLE_NAME_EXTRACTION, false)

            this
        }

        ScanActivity.scan(this, USER_SCAN_REQUEST_CODE, bndl)
    }

    private fun getOnFieldStateChangeListener(): OnFieldStateChangeListener {
        return object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {
                Log.e("vgs_collect_state", "$state ")
                when (state) {
                    is FieldState.CardNumberState -> handleCardNumberState(state)
                    is FieldState.CardExpirationDateState -> showErrorIfNotValidInput(
                        cardExpDateFieldLay,
                        state
                    )
                    is FieldState.CardHolderNameState -> showErrorIfNotValidInput(
                        cardHolderFieldLay,
                        state
                    )
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
        if (!state.isValid && !state.hasFocus && !state.isEmpty) {
            layout?.setError(getString(R.string.error_is_not_valid))
        } else {
            layout?.setError(null)
        }
    }

    private fun handleCardNumberState(state: FieldState.CardNumberState) {
        showErrorIfNotValidInput(cardNumberFieldLay, state)

        previewCardNumber?.text = state.number
        if (state.cardBrand == CardType.VISA.name) {
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
        if (vgsForm.getFileProvider().getAttachedFiles().isEmpty()) {
            attachBtn?.text = getString(R.string.collect_activity_attach_btn)
        } else {
            attachBtn?.text = getString(R.string.collect_activity_detach_btn)
        }
    }

    private fun setStateLoading(state: Boolean) {
        if (state) {
            progressBar?.visibility = View.VISIBLE
            submitBtn?.isEnabled = false
            attachBtn?.isEnabled = false
        } else {
            progressBar?.visibility = View.INVISIBLE
            submitBtn?.isEnabled = true
            attachBtn?.isEnabled = true
        }
    }

    private fun setEnabledResponseHeader(isEnabled: Boolean) {
        if (isEnabled) {
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
            R.id.revealBtn -> revealData()
            R.id.submitBtn -> submitData()
            R.id.attachBtn -> attachFile()
            R.id.passwordIcon -> applyPasswordOnVGSShowControls()
        }
    }

    private fun applyPasswordOnVGSShowControls() {
        if (showContentIsHidden) {
            showContentIsHidden = false
            revealedNumber?.isSecureText = false
            passwordIcon.setImageResource(R.drawable.ic_password_on)
        } else {
            showContentIsHidden = true
            revealedNumber?.isSecureText = true
            passwordIcon.setImageResource(R.drawable.ic_password_off)
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
        if (vgsForm.getFileProvider().getAttachedFiles().isEmpty()) {
            vgsForm.getFileProvider().attachFile("attachments.file")
        } else {
            vgsForm.getFileProvider().detachAll()
        }
        checkAttachedFiles()
    }

    private fun setupShow() {
        VGSShowLogger.isEnabled = true
        VGSShowLogger.level = VGSShowLogger.Level.DEBUG

        vgsShow.subscribe(revealedNumber!!)

        revealedNumber?.setSecureTextRange(arrayOf(VGSTextRange(5, 8), VGSTextRange(10, 13)))
        revealedNumber?.secureTextSymbol = '#'
        revealedNumber!!.addTransformationRegex(
            "(\\d{4})(\\d{4})(\\d{4})(\\d{4})".toRegex(),
            "\$1-\$2-\$3-\$4"
        )

        revealedNumber?.setOnTextChangeListener(object : VGSTextView.OnTextChangedListener {
            override fun onTextChange(view: VGSTextView, isEmpty: Boolean) {
                Log.d(MainActivity::class.simpleName, "textIsEmpty: $isEmpty")
            }
        })
        revealedNumber?.addOnCopyTextListener(object : VGSTextView.OnTextCopyListener {

            override fun onTextCopied(view: VGSTextView, format: VGSTextView.CopyTextFormat) {
                Toast.makeText(
                    applicationContext,
                    "Number text copied! format: $format",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        revealedNumber?.setOnClickListener {
            revealedNumber!!.copyToClipboard(VGSTextView.CopyTextFormat.RAW)
        }

        vgsShow.addOnResponseListener(object : VGSOnResponseListener {
            override fun onResponse(response: com.verygoodsecurity.vgsshow.core.network.model.VGSResponse) {
                setStateLoading(false)
                responseContainerView.text = "Show Response Code: \n ${response.code}"
                Log.d(MainActivity::class.simpleName, response.toString())
            }
        })
    }

    private fun revealData() {
        if (revealNumberAlias.isNotEmpty()) {
            setStateLoading(true)
            responseContainerView?.text = ""
            vgsShow.requestAsync(
                com.verygoodsecurity.vgsshow.core.network.model.VGSRequest.Builder(
                    "post",
                    VGSHttpMethod.POST
                ).body(
                    mapOf(
                        "payment_card_number" to revealNumberAlias
                    )
                ).build()
            )
        } else {
            Toast.makeText(this, R.string.error_no_data_to_reveal, Toast.LENGTH_LONG).show()
        }
    }

    var revealNumberAlias = ""
    private fun parseNumberAlias(json: JSONObject?) {
        json?.let {
            if (it.has("json") && it.getJSONObject("json").has("cardNumber")) {
                it.getJSONObject("json").getString("cardNumber").let {
                    numberToken?.text = it
                    numberToken?.visibility = View.VISIBLE
                    revealNumberAlias = it
                }
            }
        }
    }

}

typealias CollectSuccessResponse = com.verygoodsecurity.vgscollect.core.model.network.VGSResponse.SuccessResponse?