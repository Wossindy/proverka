package com.example.proverka

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var exampleTextView: TextView
    private lateinit var answerTextView: TextView
    private lateinit var resultTextView: TextView
    private lateinit var statsTextView: TextView
    private lateinit var startButton: Button
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    private var correctAnswer = 0.0
    private var correctAnswerDisplayed = 0.0
    private var startTime = 0L
    private var correctCount = 0
    private var incorrectCount = 0
    private val times = mutableListOf<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exampleTextView = findViewById(R.id.exampleTextView)
        answerTextView = findViewById(R.id.answerTextView)
        resultTextView = findViewById(R.id.resultTextView)
        statsTextView = findViewById(R.id.statsTextView)
        startButton = findViewById(R.id.startButton)
        trueButton = findViewById(R.id.trueButton)
        falseButton = findViewById(R.id.falseButton)

        startButton.setOnClickListener { generateExample() }
        trueButton.setOnClickListener { checkAnswer(true) }
        falseButton.setOnClickListener { checkAnswer(false) }
    }

    private fun generateExample() {
        val num1 = Random.nextInt(10, 100)
        val num2 = Random.nextInt(10, 100)
        val operator = listOf("+", "-", "*", "/").random()

        val example = "$num1 $operator $num2"
        exampleTextView.text = example

        correctAnswer = when (operator) {
            "+" -> num1 + num2.toDouble()
            "-" -> num1 - num2.toDouble()
            "*" -> num1 * num2.toDouble()
            "/" -> String.format("%.2f", num1 / num2.toDouble()).toDouble()
            else -> 0.0
        }

        val showCorrectAnswer = Random.nextBoolean()
        correctAnswerDisplayed = if (showCorrectAnswer) {
            correctAnswer
        } else {
            val randomOffset = listOf(-1, 1).random() * Random.nextDouble(1.0, 10.0)
            (correctAnswer + randomOffset).let {
                if (operator == "/") String.format("%.2f", it).toDouble() else it
            }
        }

        answerTextView.text = correctAnswerDisplayed.toString()
        startTime = SystemClock.elapsedRealtime()

        trueButton.isEnabled = true
        falseButton.isEnabled = true
        resultTextView.text = ""
    }

    private fun checkAnswer(isTrueSelected: Boolean) {
        val isCorrect = (correctAnswer == correctAnswerDisplayed) == isTrueSelected
        resultTextView.text = if (isCorrect) "ПРАВИЛЬНО" else "НЕ ПРАВИЛЬНО"

        val elapsedTime = SystemClock.elapsedRealtime() - startTime
        times.add(elapsedTime)

        if (isCorrect) {
            correctCount++
        } else {
            incorrectCount++
        }

        updateStats()

        trueButton.isEnabled = false
        falseButton.isEnabled = false
    }

    private fun updateStats() {
        val totalAttempts = correctCount + incorrectCount
        val accuracy = (correctCount.toDouble() / totalAttempts) * 100
        val minTime = times.minOrNull() ?: 0L
        val maxTime = times.maxOrNull() ?: 0L
        val avgTime = if (times.isNotEmpty()) times.average() else 0.0

        val statsText = """
            Правильные ответы: $correctCount
            Неправильные ответы: $incorrectCount
            Точность: ${String.format("%.2f", accuracy)}%
            Минимальное время: ${minTime}мс
            Максимальное время: ${maxTime}мс
            Среднее время: ${String.format("%.2f", avgTime)}мс
        """.trimIndent()

        statsTextView.text = statsText
    }
}
