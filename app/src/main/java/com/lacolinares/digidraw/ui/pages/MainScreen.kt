package com.lacolinares.digidraw.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lacolinares.digidraw.R
import com.lacolinares.digidraw.ui.components.DigiButton
import com.lacolinares.digidraw.ui.components.DigiText
import com.lacolinares.digidraw.ui.destinations.DigiModalDestination
import com.lacolinares.digidraw.ui.destinations.QuizScreenDestination
import com.lacolinares.digidraw.ui.theme.MineralGreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
) {
    val buttonWidth = 120
    val context = LocalContext.current
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
                DigiButton(
                    text = stringResource(R.string.start_text),
                    minWidth = buttonWidth,
                    onClick = {
                        navigator.navigate(QuizScreenDestination)
                    }
                )
                DigiButton(
                    text = stringResource(R.string.history_text),
                    minWidth = buttonWidth,
                )
                DigiButton(
                    text = stringResource(R.string.how_to_play_text),
                    minWidth = buttonWidth,
                    onClick = {
                        navigator.navigate(
                            DigiModalDestination(
                                title = context.getString(R.string.how_to_play_text),
                                message = context.getString(R.string.how_to_play_description_text)
                            )
                        )
                    }
                )
                DigiButton(
                    text = stringResource(R.string.about_text),
                    minWidth = buttonWidth,
                    onClick = {
                        navigator.navigate(
                            DigiModalDestination(
                                title = context.getString(R.string.about_text),
                                message = context.getString(R.string.about_description_text)
                            )
                        )
                    }
                )
            }
        }
    }
}