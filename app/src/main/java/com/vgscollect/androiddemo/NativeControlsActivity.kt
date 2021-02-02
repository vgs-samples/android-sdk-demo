package com.vgscollect.androiddemo

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_default_components.*

class NativeControlsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default_components)
    }

    fun addViewProgrammatically(v: View) {
        val field = EditText(this)
        field.setHint("Surname")
        field.setText("Galt")

        container.removeAllViews()
        container.addView(field)

        field.requestFocus()
    }

}