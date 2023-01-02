package com.lacolinares.digidraw.ui.pages.quiz

import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lacolinares.digidraw.R
import com.lacolinares.digidraw.ui.components.DigiButton
import com.lacolinares.digidraw.ui.components.DigiSpace
import com.lacolinares.digidraw.ui.components.DigiText
import com.lacolinares.digidraw.ui.destinations.DigiModalDestination
import com.lacolinares.digidraw.ui.pages.history.HistoryDataStore
import com.lacolinares.digidraw.ui.pages.history.HistoryModel
import com.lacolinares.digidraw.ui.theme.MineralGreen
import com.lacolinares.digidraw.ui.theme.OuterSpace
import com.lacolinares.digidraw.util.FileHelper
import com.lacolinares.digidraw.util.HandWrittenDigitView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.coroutines.launch
import org.pytorch.LiteModuleLoader
import org.pytorch.Module

@Destination
@Composable
fun QuizScreen(
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<DigiModalDestination, Boolean>,
) {
    val context = LocalContext.current
    val viewModel: QuizViewModel = viewModel()
    var clearDrawing by remember { mutableStateOf(false) }
    var submitAnswer by remember { mutableStateOf(false) }

    val question = viewModel.activeQuestion.collectAsState()

    val module = LiteModuleLoader.load(FileHelper.getVit4mnist(context))

    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}
            is NavResult.Value -> {
                module.destroy()
                navigator.popBackStack()
            }
        }
    }

    ScreenContainer {
        Header(
            onClose = {
                module.destroy()
                navigator.popBackStack()
            }
        )
        Instruction()
        DigiSpace(24)
        Question(question.value)
        DigiSpace(32)
        DrawingArea(
            module = module,
            clearDrawing = clearDrawing,
            submitAnswer = submitAnswer,
            navigator = navigator,
            viewModel = viewModel,
            onCleared = { clearDrawing = false },
            onSubmitAnswer = { submitAnswer = false }
        )
        DigiSpace(32)
        Footer(
            onSubmit = { submitAnswer = true },
            onClear = { clearDrawing = true }
        )
    }
}

@Composable
private fun Footer(
    onSubmit: () -> Unit,
    onClear: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        DigiButton(
            text = stringResource(R.string.submit_text),
            fontSize = 16.sp,
            minWidth = 40,
            verticalPadding = 8,
            horizontalPadding = 20,
            onClick = onSubmit::invoke
        )

        DigiButton(
            text = stringResource(R.string.clear_text),
            fontSize = 16.sp,
            minWidth = 40,
            verticalPadding = 8,
            horizontalPadding = 20,
            onClick = onClear::invoke
        )
    }
}

@Composable
private fun DrawingArea(
    module: Module,
    clearDrawing: Boolean,
    submitAnswer: Boolean,
    navigator: DestinationsNavigator,
    viewModel: QuizViewModel,
    onCleared: () -> Unit,
    onSubmitAnswer: () -> Unit,
) {
    val context = LocalContext.current
    val composableScope = rememberCoroutineScope()
    val historyDataStore = HistoryDataStore(context)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = OuterSpace
    ) {
        AndroidView(
            factory = {
                HandWrittenDigitView(it).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                }
            },
            update = {
                if (clearDrawing) {
                    it.clearAllPointsAndRedraw()
                    onCleared.invoke()
                }

                if (submitAnswer) {
                    composableScope.launch {
                        viewModel.onSubmit(
                            module = module,
                            allPoints = it.getAllPoints(),
                            drawViewWidth = it.width,
                            drawViewHeight = it.height,
                            onSubmitted = {
                                it.clearAllPointsAndRedraw()
                                onSubmitAnswer.invoke()
                            },
                            onFinish = { score, total ->
                                it.clearAllPointsAndRedraw()
                                onSubmitAnswer.invoke()
                                composableScope.launch {
                                    historyDataStore.setHistory(HistoryModel(score = score, totalQuestion = total))
                                }
                                navigator.navigate(
                                    DigiModalDestination(
                                        title = context.getString(R.string.game_over_text),
                                        message = context.getString(R.string.your_score_text, score, total),
                                    )
                                )
                            }
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun Question(
    question: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = OuterSpace
    ) {
        DigiText(
            text = question,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun Instruction() {
    DigiText(
        text = stringResource(R.string.instruction_text),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
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