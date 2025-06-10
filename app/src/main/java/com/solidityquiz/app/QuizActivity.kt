package com.solidityquiz.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.solidityquiz.app.data.DataManager
import com.solidityquiz.app.models.Lesson
import com.solidityquiz.app.models.QuestionType
import com.solidityquiz.app.models.QuizQuestion
import io.github.kbiakov.codeview.CodeView
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme

class QuizActivity : AppCompatActivity() {
    
    private lateinit var questionTextView: TextView
    private lateinit var codeView: CodeView
    private lateinit var answersContainer: LinearLayout
    private lateinit var btnSubmit: Button
    private lateinit var btnNext: Button
    private lateinit var progressTextView: TextView
    private lateinit var dataManager: DataManager
    
    private var currentLesson: Lesson? = null
    private var questions = listOf<QuizQuestion>()
    private var currentQuestionIndex = 0
    private var score = 0
    private var userAnswers = mutableListOf<String>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        
        initializeViews()
        loadQuiz()
    }
    
    private fun initializeViews() {
        questionTextView = findViewById(R.id.tvQuestion)
        codeView = findViewById(R.id.codeViewQuiz)
        answersContainer = findViewById(R.id.answersContainer)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnNext = findViewById(R.id.btnNext)
        progressTextView = findViewById(R.id.tvQuizProgress)
        dataManager = DataManager(this)
        
        btnSubmit.setOnClickListener { submitAnswer() }
        btnNext.setOnClickListener { nextQuestion() }
    }
    
    private fun loadQuiz() {
        val levelId = intent.getIntExtra("LEVEL_ID", 1)
        val lessonId = intent.getIntExtra("LESSON_ID", 1)
        
        currentLesson = dataManager.getLessonById(levelId, lessonId)
        questions = currentLesson?.quizQuestions ?: emptyList()
        
        if (questions.isNotEmpty()) {
            supportActionBar?.title = "Quiz: ${currentLesson?.title}"
            showQuestion(0)
        } else {
            Toast.makeText(this, "No questions available", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    
    private fun showQuestion(index: Int) {
        if (index >= questions.size) {
            showResults()
            return
        }
        
        val question = questions[index]
        currentQuestionIndex = index
        
        // Update progress
        progressTextView.text = "Question ${index + 1} of ${questions.size}"
        
        // Show question
        questionTextView.text = question.question
        
        // Show code snippet if available
        if (question.codeSnippet != null) {
            codeView.visibility = android.view.View.VISIBLE
            codeView.setOptions(Options.Default.get(this)
                .withLanguage("solidity")
                .withTheme(ColorTheme.MONOKAI)
                .withCode(question.codeSnippet))
        } else {
            codeView.visibility = android.view.View.GONE
        }
        
        // Clear previous answers
        answersContainer.removeAllViews()
        
        // Setup answer input based on question type
        when (question.type) {
            QuestionType.MULTIPLE_CHOICE -> setupMultipleChoice(question)
            QuestionType.FILL_IN_THE_BLANK -> setupFillInTheBlank(question)
            QuestionType.CODE_COMPLETION -> setupCodeCompletion(question)
        }
        
        btnSubmit.isEnabled = true
        btnNext.isEnabled = false
    }
    
    private fun setupMultipleChoice(question: QuizQuestion) {
        question.options?.forEach { option ->
            val button = Button(this)
            button.text = option
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
            button.setOnClickListener {
                // Clear other selections
                for (i in 0 until answersContainer.childCount) {
                    val child = answersContainer.getChildAt(i) as Button
                    child.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
                }
                // Highlight selected
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_light))
                button.tag = "selected"
            }
            answersContainer.addView(button)
        }
    }
    
    private fun setupFillInTheBlank(question: QuizQuestion) {
        question.blanks?.forEach { blank ->
            val textInputLayout = TextInputLayout(this)
            textInputLayout.hint = blank.placeholder
            
            val editText = TextInputEditText(this)
            editText.tag = blank.id
            
            textInputLayout.addView(editText)
            answersContainer.addView(textInputLayout)
        }
    }
    
    private fun setupCodeCompletion(question: QuizQuestion) {
        val textInputLayout = TextInputLayout(this)
        textInputLayout.hint = "Complete the code"
        
        val editText = TextInputEditText(this)
        editText.minLines = 3
        
        textInputLayout.addView(editText)
        answersContainer.addView(textInputLayout)
    }
    
    private fun submitAnswer() {
        val question = questions[currentQuestionIndex]
        val userAnswer = getUserAnswer(question)
        userAnswers.add(userAnswer)
        
        val isCorrect = checkAnswer(question, userAnswer)
        if (isCorrect) {
            score++
            showFeedback("Correct!", question.explanation, true)
        } else {
            showFeedback("Incorrect. ${question.explanation}", "Correct answer: ${question.correctAnswer}", false)
        }
        
        btnSubmit.isEnabled = false
        btnNext.isEnabled = true
    }
    
    private fun getUserAnswer(question: QuizQuestion): String {
        return when (question.type) {
            QuestionType.MULTIPLE_CHOICE -> {
                for (i in 0 until answersContainer.childCount) {
                    val button = answersContainer.getChildAt(i) as Button
                    if (button.tag == "selected") {
                        return button.text.toString()
                    }
                }
                ""
            }
            QuestionType.FILL_IN_THE_BLANK -> {
                val answers = mutableListOf<String>()
                for (i in 0 until answersContainer.childCount) {
                    val layout = answersContainer.getChildAt(i) as TextInputLayout
                    val editText = layout.editText as TextInputEditText
                    answers.add(editText.text.toString().trim())
                }
                answers.joinToString(",")
            }
            QuestionType.CODE_COMPLETION -> {
                val layout = answersContainer.getChildAt(0) as TextInputLayout
                val editText = layout.editText as TextInputEditText
                editText.text.toString().trim()
            }
        }
    }
    
    private fun checkAnswer(question: QuizQuestion, userAnswer: String): Boolean {
        return userAnswer.equals(question.correctAnswer, ignoreCase = true)
    }
    
    private fun showFeedback(title: String, message: String, isCorrect: Boolean) {
        Toast.makeText(this, "$title $message", Toast.LENGTH_LONG).show()
    }
    
    private fun nextQuestion() {
        showQuestion(currentQuestionIndex + 1)
    }
    
    private fun showResults() {
        val percentage = (score * 100) / questions.size
        val passed = percentage >= 70
        
        val intent = Intent()
        intent.putExtra("QUIZ_COMPLETED", passed)
        intent.putExtra("SCORE", score)
        intent.putExtra("TOTAL", questions.size)
        intent.putExtra("PERCENTAGE", percentage)
        
        setResult(if (passed) RESULT_OK else RESULT_CANCELED, intent)
        
        Toast.makeText(this, 
            "Quiz completed! Score: $score/${questions.size} ($percentage%)", 
            Toast.LENGTH_LONG).show()
        
        finish()
    }
}
