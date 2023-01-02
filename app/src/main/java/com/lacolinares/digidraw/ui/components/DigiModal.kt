package com.lacolinares.digidraw.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.lacolinares.digidraw.R
import com.lacolinares.digidraw.ui.theme.MineralGreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle

object DigiModalStyle: DestinationStyle.Dialog{
    override val properties: DialogProperties
        get() = DialogProperties()
}

@Destination(style = DigiModalStyle::class)
@Composable
fun DigiModal(
    navigator: DestinationsNavigator,
    title: String,
    message: String
) {
    Card(
        modifier = Modifier.wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = MineralGreen
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DigiText(text = title, fontSize = 32.sp)
            DigiText(text = message, fontSize = 18.sp)
            DigiButton(
                text = stringResource(R.string.close_text),
                verticalPadding = 8,
                onClick = {
                    navigator.popBackStack()
                }
            )
        }
    }
}