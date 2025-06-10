package com.solidityquiz.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.solidityquiz.app.adapters.ProgressAdapter
import com.solidityquiz.app.data.DataManager

class ProgressActivity : AppCompatActivity() {
    
    private lateinit var overallProgressTextView: TextView
    private lateinit var completedLevelsTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressAdapter: ProgressAdapter
    private lateinit var dataManager: DataManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)
        
        setupToolbar()
        initializeViews()
        setupRecyclerView()
        loadProgress()
    }
    
    private fun setupToolbar() {
        supportActionBar?.title = "Learning Progress"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    private fun initializeViews() {
        overallProgressTextView = findViewById(R.id.tvOverallProgress)
        completedLevelsTextView = findViewById(R.id.tvCompletedLevels)
        recyclerView = findViewById(R.id.recyclerViewProgress)
        dataManager = DataManager(this)
    }
    
    private fun setupRecyclerView() {
        progressAdapter = ProgressAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProgressActivity)
            adapter = progressAdapter
        }
    }
    
    private fun loadProgress() {
        val levels = dataManager.getAllLevels()
        
        // Calculate overall progress
        val totalLevels = levels.size
        val completedLevels = levels.count { it.isCompleted }
        val overallProgress = if (totalLevels > 0) {
            levels.sumOf { it.progress } / totalLevels
        } else 0
        
        // Update UI
        overallProgressTextView.text = "Overall Progress: $overallProgress%"
        completedLevelsTextView.text = "Completed Levels: $completedLevels/$totalLevels"
        
        // Update adapter
        progressAdapter.updateLevels(levels)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
