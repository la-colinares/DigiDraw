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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.lacolinares.digidraw.ui.components.DigiButton
import com.lacolinares.digidraw.ui.components.DigiSpace
import com.lacolinares.digidraw.ui.components.DigiText
import com.lacolinares.digidraw.ui.theme.MineralGreen
import com.lacolinares.digidraw.ui.theme.OuterSpace
import com.lacolinares.digidraw.util.HandWrittenDigitView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination()
@Composable
fun QuizScreen(
    navigator: DestinationsNavigator
) {
    var clearDrawing by remember { mutableStateOf(false) }

    ScreenContainer {
        Header(
            onClose = {
                navigator.popBackStack()
            }
        )
        Instruction()
        DigiSpace(24)
        Question("1 + 1 = ?")
        DigiSpace(32)
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
                    if (clearDrawing){
                        it.clearAllPointsAndRedraw()
                        clearDrawing = false
                    }
                }
            )
        }
        DigiSpace(32)
        Footer(
            onSubmit = {

            },
            onClear = {
                clearDrawing = true
            }
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
            text = "Submit",
            fontSize = 16.sp,
            minWidth = 40,
            verticalPadding = 8,
            horizontalPadding = 20,
            onClick = onSubmit::invoke
        )

        DigiButton(
            text = "Clear",
            fontSize = 16.sp,
            minWidth = 40,
            verticalPadding = 8,
            horizontalPadding = 20,
            onClick = onClear::invoke
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
        text = "draw the answer below the question.",
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
            text = "X",
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