package com.lacolinares.digidraw.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lacolinares.digidraw.R
import com.lacolinares.digidraw.ui.components.DigiButton
import com.lacolinares.digidraw.ui.components.DigiSpace
import com.lacolinares.digidraw.ui.components.DigiText
import com.lacolinares.digidraw.ui.destinations.DigiModalDestination
import com.lacolinares.digidraw.ui.destinations.HistoryScreenDestination
import com.lacolinares.digidraw.ui.destinations.QuizScreenDestination
import com.lacolinares.digidraw.ui.pages.history.HistoryDataStore
import com.lacolinares.digidraw.ui.theme.MineralGreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

internal class MainScreenPreviewParam: PreviewParameterProvider<DestinationsNavigator>{
    override val values: Sequence<DestinationsNavigator>
        get() = sequenceOf(EmptyDestinationsNavigator)
}

@Preview(showSystemUi = true)
@Destination(start = true)
@Composable
fun MainScreen(
    @PreviewParameter(MainScreenPreviewParam::class) navigator: DestinationsNavigator,
) {
    val buttonWidth = 220
    val buttonVerticalPadding = 12
    val buttonHorizontalPadding = 10
    val buttonFontSize = 28.sp
    val context = LocalContext.current
    val historyDataStore = HistoryDataStore(context)
    val highestScore = historyDataStore.highestScore.collectAsState(initial = 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MineralGreen),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DigiSpace(height = 40)
        DigiText(
            text = stringResource(id = R.string.app_name),
            fontSize = 48.sp
        )
        if (highestScore.value > 0){
            DigiSpace(height = 40)
            DigiText(
                text = stringResource(id = R.string.high_score_text, highestScore.value),
                fontSize = 20.sp
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                DigiButton(
                    text = stringResource(id = R.string.start_text),
                    fontSize = buttonFontSize,
                    minWidth = buttonWidth,
                    verticalPadding = buttonVerticalPadding,
                    horizontalPadding = buttonHorizontalPadding,
                    onClick = { navigator.navigate(QuizScreenDestination) }
                )
                DigiButton(
                    text = stringResource(id = R.string.history_text),
                    fontSize = buttonFontSize,
                    minWidth = buttonWidth,
                    verticalPadding = buttonVerticalPadding,
                    horizontalPadding = buttonHorizontalPadding,
                    onClick = { navigator.navigate(HistoryScreenDestination) }
                )
                DigiButton(
                    text = stringResource(id = R.string.how_to_play_text),
                    fontSize = buttonFontSize,
                    minWidth = buttonWidth,
                    verticalPadding = buttonVerticalPadding,
                    horizontalPadding = buttonHorizontalPadding,
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
                    text = stringResource(id = R.string.about_text),
                    fontSize = buttonFontSize,
                    minWidth = buttonWidth,
                    verticalPadding = buttonVerticalPadding,
                    horizontalPadding = buttonHorizontalPadding,
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