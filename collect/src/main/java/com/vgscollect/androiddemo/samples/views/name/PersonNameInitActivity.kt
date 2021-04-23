package com.vgscollect.androiddemo.samples.views.name

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscollect.view.card.validation.rules.PersonNameRule
import com.verygoodsecurity.vgscollect.widget.PersonNameEditText
import com.vgscollect.androiddemo.R
import kotlinx.android.synthetic.main.activity_layout.*

class PersonNameInitActivity : AppCompatActivity() {

    private lateinit var vgsCollect: VGSCollect

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        // Init collect
        vgsCollect = VGSCollect(this, "<vault_id>", "<environment>")

        // Create person name view
        val vgsEtPersonName = PersonNameEditText(this).apply {
            setFieldName("<field_name>")
            setHint("Person name")
            setPadding(resources.getDimensionPixelSize(R.dimen.padding))
        }
        rootView.addView(vgsEtPersonName)

        // Subscribe view
        vgsCollect.bindView(vgsEtPersonName)

        // Add person name validation rule, supported from VGS Collect SDK v1.1.16 or higher
        vgsEtPersonName.addRule(
            PersonNameRule.ValidationBuilder()
                .setAllowableMinLength(3)
                .setAllowableMaxLength(20)
                .build()
        )

        // Add field state change listener
        vgsEtPersonName.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {

            override fun onStateChange(state: FieldState) {
                Log.d(PersonNameInitActivity::class.simpleName, state.toString())
            }
        })
    }
}