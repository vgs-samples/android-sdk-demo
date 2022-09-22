package com.vgscollect.androiddemo.samples.views.layout

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.google.android.material.textfield.TextInputLayout
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscollect.widget.VGSEditText
import com.verygoodsecurity.vgscollect.widget.VGSTextInputLayout
import com.vgscollect.androiddemo.R

class InputLayoutActivity : AppCompatActivity() {

    private lateinit var vgsCollect: VGSCollect

    private val rootView: FrameLayout by lazy { findViewById(R.id.rootView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        // Init collect
        vgsCollect = VGSCollect(this, "<vault_id>", "<environment>")

        // Create & configure text inout layout
        val vgsInputLayout = VGSTextInputLayout(this).apply {
            setErrorEnabled(false)
            setHint("Address")
            // Configure box style
            setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE)
            setBoxStrokeColor(Color.CYAN)
            with(resources.getDimension(R.dimen.padding)) {
                setBoxCornerRadius(
                    this,
                    this,
                    this,
                    this
                )
            }
        }

        // Create edit text view
        val vgsEditText = VGSEditText(this).apply {
            setFieldName("<field_name>")
            setPadding(resources.getDimensionPixelSize(R.dimen.padding))
        }

        // Add views
        vgsInputLayout.addView(vgsEditText)
        rootView.addView(vgsInputLayout)

        // Subscribe view
        vgsCollect.bindView(vgsEditText)

        // Add field state change listener
        vgsEditText.setOnFieldStateChangeListener(object : OnFieldStateChangeListener {

            override fun onStateChange(state: FieldState) {
                Log.d(InputLayoutActivity::class.simpleName, state.toString())
            }
        })
    }
}