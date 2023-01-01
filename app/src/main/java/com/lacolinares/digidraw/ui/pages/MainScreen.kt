package com.lacolinares.digidraw.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lacolinares.digidraw.R
import com.lacolinares.digidraw.ui.components.DigiButton
import com.lacolinares.digidraw.ui.components.DigiText
import com.lacolinares.digidraw.ui.theme.MineralGreen
import com.ramcosta.composedestinations.annotation.Destination

@Destination(start = true)
@Composable
fun MainScreen() {
    val buttonWidth = 120
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MineralGreen),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        DigiText(
            text = stringResource(id = R.string.app_name),
            fontSize = 48.sp
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                DigiButton(text = "Start", minWidth = buttonWidth)
                DigiButton(text = "History", minWidth = buttonWidth)
                DigiButton(text = "How To Play", minWidth = buttonWidth)
                DigiButton(text = "About", minWidth = buttonWidth)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainPreview() {
    MainScreen()
}