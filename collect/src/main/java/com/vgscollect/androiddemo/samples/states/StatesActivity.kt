package com.vgscollect.androiddemo.samples.states

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.vgscollect.androiddemo.R

class StatesActivity : AppCompatActivity() {

    private lateinit var vgsForm: VGSCollect

    private val field: VGSCardNumberEditText by lazy { findViewById(R.id.field) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_states)

        // Initialize VGSCollect
        vgsForm = VGSCollect(this, "<vault_id>", "<environment>")

        //set listener to observe states from all fields
        vgsForm.addOnFieldStateChangeListener(object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {

            }
        })

        //set OnFieldStateChangeListener directly on field. In this listener you will receive state only from specific field
        field.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {
            override fun onStateChange(state: FieldState) {

            }
        })

        //You can retrieve current state directly from field
        val state = field.getState()
    }
}