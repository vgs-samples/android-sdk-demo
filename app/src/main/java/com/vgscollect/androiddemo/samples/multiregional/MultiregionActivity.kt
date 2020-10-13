package com.vgscollect.androiddemo.samples.multiregional

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscollect.core.Environment
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.vgscollect.androiddemo.R

class MultiregionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_states)

        // Initialize VGSCollect
        initializeCollectOnUS()
        initializeCollectOnEU()
    }

    private fun initializeCollectOnUS() {
        //initialize Collect with Enum
        VGSCollect(this, "<vault_id>", Environment.LIVE)
        VGSCollect(this, "<vault_id>", Environment.SANDBOX)

        //initialize Collect with String
        VGSCollect(this, "<vault_id>", "live")
        VGSCollect(this, "<vault_id>", "sandbox")
    }

    private fun initializeCollectOnEU() {
        //initialize Collect with String
        VGSCollect(this, "<vault_id>", "live-eu-0")
        VGSCollect(this, "<vault_id>", "sandbox-eu-0")
    }
}