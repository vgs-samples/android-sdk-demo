package com.vgscollect.androiddemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscollect.widget.PersonNameEditText
import kotlinx.android.synthetic.main.activity_collect_components.*

class CollectActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect_components)
    }

    fun addViewProgrammatically(v: View) {
        val field = PersonNameEditText(this)
        field.setHint("Surname")
        field.setText("Galt")

        container.removeAllViews()
        container.addView(field)

        field.requestFocus()
    }

}