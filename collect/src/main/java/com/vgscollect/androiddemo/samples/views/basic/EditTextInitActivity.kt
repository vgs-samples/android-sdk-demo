package com.vgscollect.androiddemo.samples.views.basic

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscollect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscollect.widget.VGSEditText
import com.vgscollect.androiddemo.R
import kotlinx.android.synthetic.main.activity_layout.*

class EditTextInitActivity : AppCompatActivity() {

    private val vgsCollect: VGSCollect by lazy {
        VGSCollect.Builder(this, "<VAULT_ID>").create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        // Create edit text view
        val vgsEditText = VGSEditText(this).apply {
            setFieldName("<FIELD_NAME>")
            setHint("Address")
            setPadding(resources.getDimensionPixelSize(R.dimen.padding))
        }
        rootView.addView(vgsEditText)

        // Subscribe view
        vgsCollect.bindView(vgsEditText)

        // Add validation rule, supported from VGS Collect SDK v1.6.5
        vgsEditText.addRule(
           VGSInfoRule.ValidationBuilder()
               .setAllowableMinLength(10)
               .setAllowableMaxLength(30)
               .build()
        )

        // Add field state change listener
        vgsEditText.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {

            override fun onStateChange(state: FieldState) {
                Log.d(EditTextInitActivity::class.simpleName, state.toString())
            }
        })
    }
}