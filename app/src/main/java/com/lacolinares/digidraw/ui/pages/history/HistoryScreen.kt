package com.lacolinares.digidraw.ui.pages.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.lacolinares.digidraw.R
import com.lacolinares.digidraw.ui.components.DigiText
import com.lacolinares.digidraw.ui.theme.MineralGreen
import com.lacolinares.digidraw.ui.theme.OuterSpace
import com.lacolinares.digidraw.ui.theme.White
import com.lacolinares.digidraw.util.toTimeAgo
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

internal class HistoryScreenPreviewParam : PreviewParameterProvider<DestinationsNavigator> {
    override val values: Sequence<DestinationsNavigator>
        get() = sequenceOf(EmptyDestinationsNavigator)
}

@Preview(showBackground = true)
@Destination
@Composable
fun HistoryScreen(
    @PreviewParameter(HistoryScreenPreviewParam::class) navigator: DestinationsNavigator,
) {

    val context = LocalContext.current
    val historyDataStore = HistoryDataStore(context)
    val histories = historyDataStore.history.collectAsState(initial = emptyList())

    val listState = rememberLazyListState()
    
    ScreenContainer {
        Header(
            onClose = {
                navigator.popBackStack()
            }
        )
        DigiText(
            text = stringResource(id = R.string.history_text),
            fontSize = 40.sp
        )
        if (histories.value.isEmpty()){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                DigiText(text = stringResource(R.string.no_history_text))
            }
        }else{
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
            ){
                items(
                    items = histories.value,
                    key = { history -> history.hashCode() }
                ){ history ->
                    HistoryCard(model = history)
                }
            }
        }
    }
}

@Composable
fun HistoryCard(model: HistoryModel) {
    val score = "you scored: ${model.getFormattedScoreValue()}"
    val date = model.date.toTimeAgo()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(4.dp),
        backgroundColor = OuterSpace,
        elevation = 8.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (scoreRef, dateRef) = createRefs()

            val scoreModifier = Modifier.constrainAs(scoreRef) {
                start.linkTo(parent.start)
                end.linkTo(dateRef.start, margin = 16.dp)
                top.linkTo(parent.top)
            }

            val dateModifier = Modifier.constrainAs(dateRef){
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }

            DigiText(
                text = score,
                modifier = scoreModifier,
                textAlign = TextAlign.Start
            )
            Text(
                text = date,
                color = White,
                modifier = dateModifier,
                fontSize = 12.sp
            )
        }

    }
}

@Composable
private fun Header(
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        DigiText(
            text = stringResource(R.string.cross_text),
            fontSize = 32.sp,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .clickable {
                    onClose.invoke()
                }
        )
    }
}

@Composable
private fun ScreenContainer(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MineralGreen),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}