package com.vgscollect.androiddemo.samples.compare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.vgscollect.androiddemo.R

class CompareContentActivity : AppCompatActivity() {

    private lateinit var vgsForm: VGSCollect

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare_content)

        // Initialize VGSCollect
        vgsForm = VGSCollect(this, "<vault_id>", "<environment>")
    }
}