package com.solidityquiz.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.solidityquiz.app.adapters.LessonAdapter
import com.solidityquiz.app.data.DataManager
import com.solidityquiz.app.models.Level
import com.solidityquiz.app.models.Lesson

class LessonActivity : AppCompatActivity() {
    
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var lessonAdapter: LessonAdapter
    private lateinit var dataManager: DataManager
    private lateinit var btnStartQuiz: Button
    
    private var currentLevel: Level? = null
    private var currentLessonIndex = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        
        initializeViews()
        setupRecyclerView()
        loadLevel()
    }
    
    private fun initializeViews() {
        titleTextView = findViewById(R.id.tvLessonTitle)
        descriptionTextView = findViewById(R.id.tvLessonDescription)
        recyclerView = findViewById(R.id.recyclerViewLessons)
        btnStartQuiz = findViewById(R.id.btnStartQuiz)
        dataManager = DataManager(this)
        
        btnStartQuiz.setOnClickListener {
            startQuizForCurrentLesson()
        }
    }
    
    private fun setupRecyclerView() {
        lessonAdapter = LessonAdapter { lesson ->
            currentLessonIndex = currentLevel?.lessons?.indexOf(lesson) ?: 0
            updateCurrentLesson()
        }
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@LessonActivity)
            adapter = lessonAdapter
        }
    }
    
    private fun loadLevel() {
        val levelId = intent.getIntExtra("LEVEL_ID", 1)
        currentLevel = dataManager.getLevelById(levelId)
        
        currentLevel?.let { level ->
            supportActionBar?.title = level.title
            titleTextView.text = level.title
            descriptionTextView.text = level.description
            
            lessonAdapter.updateLessons(level.lessons)
            
            if (level.lessons.isNotEmpty()) {
                updateCurrentLesson()
            }
        }
    }
    
    private fun updateCurrentLesson() {
        val lessons = currentLevel?.lessons ?: return
        if (currentLessonIndex < lessons.size) {
            val currentLesson = lessons[currentLessonIndex]
            lessonAdapter.setCurrentLesson(currentLessonIndex)
            btnStartQuiz.isEnabled = true
            btnStartQuiz.text = "Start Quiz for: ${currentLesson.title}"
        }
    }
    
    private fun startQuizForCurrentLesson() {
        val currentLesson = currentLevel?.lessons?.get(currentLessonIndex) ?: return
        
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("LEVEL_ID", currentLevel?.id)
        intent.putExtra("LESSON_ID", currentLesson.id)
        startActivityForResult(intent, REQUEST_CODE_QUIZ)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == REQUEST_CODE_QUIZ && resultCode == RESULT_OK) {
            val quizCompleted = data?.getBooleanExtra("QUIZ_COMPLETED", false) ?: false
            
            if (quizCompleted) {
                // Mark lesson as completed
                val levelId = currentLevel?.id ?: return
                val lessonId = currentLevel?.lessons?.get(currentLessonIndex)?.id ?: return
                dataManager.markLessonCompleted(levelId, lessonId)
                
                // Move to next lesson or finish level
                if (currentLessonIndex < (currentLevel?.lessons?.size ?: 0) - 1) {
                    currentLessonIndex++
                    updateCurrentLesson()
                } else {
                    // Level completed
                    finish()
                }
            }
        }
    }
    
    companion object {
        private const val REQUEST_CODE_QUIZ = 1001
    }
}
