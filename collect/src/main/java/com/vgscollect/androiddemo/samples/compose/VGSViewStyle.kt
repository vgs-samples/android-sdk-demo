package com.vgscollect.androiddemo.samples.compose

import com.verygoodsecurity.vgscollect.R as VGSResource

enum class VGSViewStyle constructor(
    val inputLayoutStyle: Int,
    val inoutViewStyle: Int
) {

    MATERIAL3_OUTLINE(
        VGSResource.style.VGSCollect_Widget_Material3_TextInputLayout_OutlineBox,
        VGSResource.style.VGSCollect_Widget_Material3_TextInputEditText_OutlineBox,
    )
}