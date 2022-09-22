package com.vgscollect.androiddemo.samples.views.ssn

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscollect.widget.SSNEditText
import com.vgscollect.androiddemo.R

class SSNInitActivity : AppCompatActivity() {

    private lateinit var vgsForm: VGSCollect

    private lateinit var ssn2: SSNEditText
    private val ssn1: SSNEditText by lazy { findViewById(R.id.ssnFromLayout) }
    private val parentLayout: LinearLayout by lazy { findViewById(R.id.parentLayout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ssn_field)

        // Initialize VGSCollect
        vgsForm = VGSCollect(this, "<vault_id>", "<environment>")

        createFieldProgrammatically()

        bindFieldToVGSCollect()
    }

    // Initialize SSN field programmatically.
    fun createFieldProgrammatically() {
        ssn2 = SSNEditText(this)
        parentLayout.addView(ssn2)
    }

    // Bind fields to the VGSCollect.
    // After this action your field become available for VGSCollect. Only now you can send user data on Proxy server.
    private fun bindFieldToVGSCollect() {
        vgsForm.bindView(ssn1)
        vgsForm.bindView(ssn2)
    }

    fun manageField() {

        // field allows you to retrieve current its state.
        val state: FieldState.SSNNumberState? = ssn2.getState()

        // Allows you to track runtime field state.
        ssn2.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {

            }
        })

        //Set <fieldName> programmatically
        ssn2.setFieldName("<fieldName>")

        ssn2.setFieldName("<fieldName>")

    }
}