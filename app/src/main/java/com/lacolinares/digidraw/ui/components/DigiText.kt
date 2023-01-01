package com.lacolinares.digidraw.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lacolinares.digidraw.ui.theme.White
import com.lacolinares.digidraw.ui.theme.marker


internal class DigiTextPreviewParam: PreviewParameterProvider<String>{
    override val values: Sequence<String>
        get() = sequenceOf("FooBar")
}

@Preview(showBackground = true, backgroundColor = 0xFF455B56)
@Composable
fun DigiText(
    @PreviewParameter(DigiTextPreviewParam::class) text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    textAlign: TextAlign = TextAlign.Center,
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)){
        Text(
            text = text,
            fontFamily = marker,
            color = White,
            fontSize = fontSize,
            modifier = modifier,
            textAlign = textAlign
        )
    }
}