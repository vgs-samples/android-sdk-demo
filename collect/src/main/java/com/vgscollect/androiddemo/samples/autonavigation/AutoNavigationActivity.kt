package com.vgscollect.androiddemo.samples.autonavigation

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscollect.VGSCollectLogger
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscollect.view.InputFieldView
import com.vgscollect.androiddemo.R
import kotlinx.android.synthetic.main.activity_autonavigation.*

class AutoNavigationActivity : AppCompatActivity(), VgsCollectResponseListener {

    private val vgsCollect: VGSCollect by lazy {
        VGSCollect.Builder(this, "<VAULT_ID>").create().apply {
            addOnResponseListeners(this@AutoNavigationActivity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autonavigation)
        VGSCollectLogger.logLevel = VGSCollectLogger.Level.DEBUG
        initView()
    }

    private var isCardNumberAutoNavigationPermitted = true
    private var isExpirationDateAutoNavigationPermitted = true
    private fun initView() {
        configureCardHolderName()
        configureCardNumber()
        configureExpirationDate()
        configureVerificationCode()
    }

    private fun configureVerificationCode() {
        vgsCollect.bindView(verificationCode)
        verificationCode?.setOnEditorActionListener(object : InputFieldView.OnEditorActionListener {
            override fun onEditorAction(v: View?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submit()
                }
                return false;
            }
        })
    }

    private fun submit() {
        showProgress()

        vgsCollect.asyncSubmit(
            VGSRequest.VGSRequestBuilder()
                .setPath("/post")
                .build()
        )
    }

    private fun configureExpirationDate() {
        expirationDate?.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {
                if (state.isValid && isExpirationDateAutoNavigationPermitted) {
                    if (state.hasFocus) {
                        isExpirationDateAutoNavigationPermitted = false
                    }
                    verificationCode?.requestFocus()
                }
            }
        })
        vgsCollect.bindView(expirationDate)
    }

    private fun configureCardNumber() {
        cardNumber?.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {
                if (state.isValid && isCardNumberAutoNavigationPermitted) {
                    if (state.hasFocus) {
                        isCardNumberAutoNavigationPermitted = false
                    }
                    expirationDate?.requestFocus()
                }
            }
        })
        vgsCollect.bindView(cardNumber)
    }

    private fun configureCardHolderName() {
        vgsCollect.bindView(cardHolderName)
        cardHolderName?.requestFocus()
    }

    override fun onResponse(response: VGSResponse?) {
        if(response?.code == 200) {
            hideProgress()
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show()

            clearAllViews()
        }
    }

    private fun hideProgress() {
        progressBar?.visibility = View.INVISIBLE
        cardHolderName?.isEnabled = true
        cardNumber?.isEnabled = true
        expirationDate?.isEnabled = true
        verificationCode?.isEnabled = true
    }

    private fun showProgress() {
        progressBar?.visibility = View.VISIBLE
        cardHolderName?.isEnabled = false
        cardNumber?.isEnabled = false
        expirationDate?.isEnabled = false
        verificationCode?.isEnabled = false
    }

    private fun clearAllViews() {
        cardHolderName?.setText("")
        cardNumber?.setText("")
        expirationDate?.setText("")
        verificationCode?.setText("")
    }

    override fun onResume() {
        super.onResume()
        cardHolderName?.postDelayed({ cardHolderName?.showKeyboard() }, 500L)
    }
}