package com.vgscollect.androiddemo.samples.compose

import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscollect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscollect.widget.VGSTextInputLayout
import com.vgscollect.androiddemo.samples.compose.theme.ComposeExampleTheme
import kotlinx.coroutines.launch

class ComposeActivity: ComponentActivity() {

    private val collect: VGSCollect by lazy {
        VGSCollect(this, "tntt1rsray8")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeExampleTheme {

                val coroutineScope = rememberCoroutineScope()
                val context = LocalContext.current

                var cardNumberView by remember { mutableStateOf<VGSCardNumberEditText?>(null) }
                var cvcView by remember { mutableStateOf<CardVerificationCodeEditText?>(null) }

                collect.bindView(cardNumberView, cvcView)

                DisposableEffect(Unit) {
                    onDispose {
                        collect.unbindView(cardNumberView)
                        collect.unbindView(cvcView)
                    }
                }

                Log.d("Test", "recomposition, cardNumberView = $cardNumberView, cvcView = $cvcView")

                Content(
                    onCardNumberViewCreated = { cardNumberView = it },
                    onCVCViewCreated = { cvcView = it },
                    onSubmitClick = {
                        coroutineScope.launch {
                            val result = collect.submitAsync("/post")
                            Toast.makeText(
                                context,
                                if (result is VGSResponse.SuccessResponse) "Success" else "Failure",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        collect.onDestroy()
    }
}

@Composable
fun Content(
    onCardNumberViewCreated: (VGSCardNumberEditText) -> Unit,
    onCVCViewCreated: (CardVerificationCodeEditText) -> Unit,
    onSubmitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        VGSCardNumberEditText(
            style = VGSViewStyle.MATERIAL3_OUTLINE,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            onViewCreated = onCardNumberViewCreated
        )
        Spacer(modifier = Modifier.height(16.dp))
        VGSCVVEditText(
            style = VGSViewStyle.MATERIAL3_OUTLINE,
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            onViewCreated = onCVCViewCreated
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = onSubmitClick
        ) {
            Text("Submit", fontSize = 18.sp)
        }
    }
}

@Composable
fun VGSCardNumberEditText(
    style: VGSViewStyle,
    modifier: Modifier = Modifier,
    onViewCreated: (VGSCardNumberEditText) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            VGSTextInputLayout(
                ContextThemeWrapper(context, style.inputLayoutStyle),
                null,
                style.inputLayoutStyle
            )
        },
        update = { view ->
            val cardNumber = VGSCardNumberEditText(
                ContextThemeWrapper(view.context, style.inoutViewStyle),
                null,
                style.inoutViewStyle
            )
            cardNumber.setDivider(' ')
            cardNumber.setHint("CARD NUMBER")
            cardNumber.setFieldName("card.number")
            view.addView(cardNumber)
            onViewCreated(cardNumber)
        }
    )
}

@Composable
fun VGSCVVEditText(
    style: VGSViewStyle,
    modifier: Modifier = Modifier,
    onViewCreated: (CardVerificationCodeEditText) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            VGSTextInputLayout(
                ContextThemeWrapper(context, style.inputLayoutStyle),
                null,
                style.inputLayoutStyle
            )
        },
        update = { view ->
            val cvc = CardVerificationCodeEditText(
                ContextThemeWrapper(view.context, style.inoutViewStyle),
                null,
                style.inoutViewStyle
            )
            cvc.setHint("CVC")
            cvc.setFieldName("card.cvc")
            view.addView(cvc)
            onViewCreated(cvc)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeExampleTheme {
        Content(
            onCardNumberViewCreated = {},
            onCVCViewCreated = {},
            onSubmitClick = {}
        )
    }
}