package com.vgscollect.androiddemo.samples.fileprovider

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.storage.content.file.VGSFileProvider
import com.vgscollect.androiddemo.R

class FileProviderActivity : AppCompatActivity() {

    private lateinit var vgsForm: VGSCollect
    private lateinit var fileProvider: VGSFileProvider

    private val attachBtn: Button by lazy { findViewById(R.id.attachBtn) }
    private val detachBtn: Button by lazy { findViewById(R.id.detachBtn) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_provider)

        // Initialize VGSCollect
        vgsForm = VGSCollect(this, "<vault_id>", "<environment>")

        //retrieve provider for managing files
        fileProvider = vgsForm.getFileProvider()

        attachBtn.setOnClickListener {
            attachFileToCollect()
        }

        detachBtn.setOnClickListener {
            detachFiles()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        vgsForm.onActivityResult(requestCode, resultCode, data)
    }

    private fun attachFileToCollect() {
        //Attach file to the Collect SDK. FIELD_NAME - it is a key under which the file for JSON will be saved before sending.
        fileProvider.attachFile(this, "<field_name>")
    }

    private fun detachFiles() {
        fileProvider.detachAll()
    }

    private fun detachFilesOneByOne() {
        val files = fileProvider.getAttachedFiles()

        files.forEach {
            fileProvider.detachFile(it)
        }
    }

}