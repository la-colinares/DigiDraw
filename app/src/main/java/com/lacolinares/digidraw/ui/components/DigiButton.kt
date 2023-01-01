package com.lacolinares.digidraw.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lacolinares.digidraw.ui.theme.OuterSpace


internal class DigiButtonPreviewParam : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf("Android")
}

@Preview(showBackground = true, backgroundColor = 0xFF455B56, showSystemUi = true)
@Composable
fun DigiButton(
    @PreviewParameter(DigiButtonPreviewParam::class) text: String,
    fontSize: TextUnit = 12.sp,
    textAlign: TextAlign = TextAlign.Center,
    minWidth: Int = 220,
) {
    Card(
        shape = RoundedCornerShape(50.dp),
        elevation = 16.dp,
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth(),
        backgroundColor = OuterSpace
    ) {
        DigiText(
            text = text,
            fontSize = fontSize,
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 12.dp)
                .defaultMinSize(minWidth = minWidth.dp),
            textAlign = textAlign
        )
    }
}