package com.solidityquiz.app.models

data class Level(
    val id: Int,
    val title: String,
    val description: String,
    val iconResource: Int,
    val lessons: List<Lesson>,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val progress: Int = 0 // 0-100
)

data class Lesson(
    val id: Int,
    val levelId: Int,
    val title: String,
    val description: String,
    val codeContent: String,
    val language: String = "solidity",
    val explanation: String,
    val quizQuestions: List<QuizQuestion>,
    val isCompleted: Boolean = false
)

data class QuizQuestion(
    val id: Int,
    val lessonId: Int,
    val type: QuestionType,
    val question: String,
    val codeSnippet: String? = null,
    val options: List<String>? = null, // For multiple choice
    val correctAnswer: String,
    val explanation: String,
    val blanks: List<BlankField>? = null // For fill-in-the-blank
)

data class BlankField(
    val id: Int,
    val placeholder: String,
    val correctAnswer: String,
    val position: Int // Position in the code snippet
)

enum class QuestionType {
    MULTIPLE_CHOICE,
    FILL_IN_THE_BLANK,
    CODE_COMPLETION
}

data class UserProgress(
    val levelId: Int,
    val lessonId: Int,
    val questionId: Int,
    val isCompleted: Boolean,
    val score: Int,
    val attempts: Int,
    val lastAttemptDate: Long
)
