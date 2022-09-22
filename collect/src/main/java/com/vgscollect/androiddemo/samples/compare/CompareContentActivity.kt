package com.vgscollect.androiddemo.samples.compare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.view.InputFieldView
import com.verygoodsecurity.vgscollect.widget.VGSEditText
import com.verygoodsecurity.vgscollect.widget.VGSTextInputLayout
import com.vgscollect.androiddemo.R

class CompareContentActivity : AppCompatActivity() {

    private lateinit var vgsForm: VGSCollect

    private val vgsEtPassword: VGSEditText by lazy { findViewById(R.id.vgsEtPassword) }
    private val vgsEtVerifyPassword: VGSEditText by lazy { findViewById(R.id.vgsEtVerifyPassword) }
    private val vgsTilVerifyPassword: VGSTextInputLayout by lazy { findViewById(R.id.vgsTilVerifyPassword) }
    private val btnSubmit: MaterialButton by lazy { findViewById(R.id.btnSubmit) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare_content)

        vgsForm = VGSCollect(this, "<vault_id>", "<environment>")

        vgsForm.bindView(vgsEtPassword) // Bind a view that will be sent to a back-end.

        vgsEtVerifyPassword.addOnTextChangeListener(object : InputFieldView.OnTextChangedListener {

            override fun onTextChange(view: InputFieldView, isEmpty: Boolean) {
                vgsTilVerifyPassword.setError(null)
            }
        })

        btnSubmit.setOnClickListener {
            if (vgsEtPassword.isContentEquals(vgsEtVerifyPassword)) {
                vgsForm.asyncSubmit("<path>", HTTPMethod.POST)
            } else {
                vgsTilVerifyPassword.setError("Password doesn't match!")
            }
        }
    }
}