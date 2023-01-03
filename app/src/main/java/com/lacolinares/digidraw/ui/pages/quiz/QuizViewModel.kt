package com.lacolinares.digidraw.ui.pages.quiz

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import java.nio.FloatBuffer
import kotlin.math.exp

class QuizViewModel : ViewModel() {

    private val questions = loadQuestions()
    private var questionPos = 0
    private var correctAnswerCount = 0
    val activeQuestion = MutableStateFlow("")

    init {
        updateQuestion()
    }

    private fun totalQuestions(): Int = questions.size

    private fun recognizeAnswer(
        module: Module,
        allPoints: List<List<Pair<Float, Float>>>,
        drawViewWidth: Int,
        drawViewHeight: Int,
        onRecognize: (Int) -> Unit,
    ) {
        if (allPoints.isEmpty()) onRecognize.invoke(-1)

        val inputs = DoubleArray(MNIST_IMAGE_SIZE * MNIST_IMAGE_SIZE)
        for (i in inputs.indices) inputs[i] = BLANK.toDouble()

        // loop through each stroke
        for (cp in allPoints) {
            for (pair in cp) {
                if (pair.first.toInt() > drawViewWidth || pair.second.toInt() > drawViewHeight || pair.first.toInt() < 0 || pair.second.toInt() < 0) continue
                val x: Int = MNIST_IMAGE_SIZE * pair.first.toInt() / drawViewWidth
                val y: Int = MNIST_IMAGE_SIZE * pair.second.toInt() / drawViewHeight
                val loc = y * MNIST_IMAGE_SIZE + x
                inputs[loc] = NON_BLANK.toDouble()
            }
        }

        val inTensorBuffer: FloatBuffer = Tensor.allocateFloatBuffer(28 * 28)
        for (`val` in inputs) inTensorBuffer.put(`val`.toFloat())

        val inTensor = Tensor.fromBlob(inTensorBuffer, longArrayOf(1, 1, 28, 28))

        val outTensor: Tensor = module.forward(IValue.from(inTensor)).toTensor()
        val outputs = outTensor.dataAsFloatArray

        var sum = 0.0f
        for (`val` in outputs) sum += exp(`val`.toDouble()).toFloat()
        for (i in outputs.indices) outputs[i] = (exp(outputs[i].toDouble()) / sum).toFloat()

        var maxScore = -Float.MAX_VALUE
        var maxScoreIdx = -1
        for (i in outputs.indices) {
            if (outputs[i] > maxScore) {
                maxScore = outputs[i]
                maxScoreIdx = i
            }
        }

        onRecognize.invoke(maxScoreIdx)
    }

    fun onSubmit(
        module: Module,
        allPoints: List<List<Pair<Float, Float>>>,
        drawViewWidth: Int,
        drawViewHeight: Int,
        onSubmitted: () -> Unit,
        onFinish: (score: Int, totalQuestion: Int) -> Unit,
    ) {
        recognizeAnswer(
            module = module,
            allPoints = allPoints,
            drawViewWidth = drawViewWidth,
            drawViewHeight = drawViewHeight,
            onRecognize = { answer ->
                val correctAnswer = questions[questionPos].correctAnswer
                if (answer.toString() == correctAnswer) {
                    correctAnswerCount++
                    Log.d("QuizViewModel", "Answer is correct")
                } else {
                    Log.e("QuizViewModel", "Answer $answer is incorrect")
                }

                if (questionPos == (questions.size - 1)) {
                    onFinish.invoke(correctAnswerCount, totalQuestions())
                    clearQuiz()
                } else {
                    questionPos++
                    updateQuestion()
                    onSubmitted.invoke()
                }
            }
        )
    }

    private fun clearQuiz() {
        correctAnswerCount = 0
        questionPos = 0
        activeQuestion.update { "" }
    }

    private fun updateQuestion() {
        activeQuestion.update {
            "${questions[questionPos].question} = ?"
        }
    }

    private fun loadQuestions(): List<QuizModel> {
        val addition = (1..4).map {
            QuizModel(question = "$it + $it", correctAnswer = "${it + it}")
        }

        val subtraction = listOf(
            QuizModel(question = "5 - 4", correctAnswer = "1"),
            QuizModel(question = "6 - 3", correctAnswer = "3"),
            QuizModel(question = "8 - 3", correctAnswer = "5"),
            QuizModel(question = "9 - 2", correctAnswer = "7"),
        )

        val multiplication = listOf(
            QuizModel(question = "2 x 2", correctAnswer = "4"),
            QuizModel(question = "4 x 2", correctAnswer = "8"),
            QuizModel(question = "3 x 3", correctAnswer = "9"),
            QuizModel(question = "8 x 0", correctAnswer = "0"),
        )

        val division = listOf(
            QuizModel(question = "8 / 2", correctAnswer = "4"),
            QuizModel(question = "4 / 2", correctAnswer = "2"),
            QuizModel(question = "18 / 3", correctAnswer = "6"),
        )


        return (addition + subtraction + multiplication + division).shuffled()
    }

    companion object {
        private const val MNISI_STD = 0.1307f
        private const val MNISI_MEAN = 0.3081f
        private const val BLANK = -MNISI_STD / MNISI_MEAN
        private const val NON_BLANK = (1.0f - MNISI_STD) / MNISI_MEAN
        private const val MNIST_IMAGE_SIZE = 28
    }

}