package com.solidityquiz.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.solidityquiz.app.adapters.LevelAdapter
import com.solidityquiz.app.data.DataManager
import com.solidityquiz.app.models.Level

class MainActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var levelAdapter: LevelAdapter
    private lateinit var dataManager: DataManager
    private lateinit var fabProgress: FloatingActionButton
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        setupToolbar()
        initializeViews()
        setupRecyclerView()
        loadLevels()
    }
    
    private fun setupToolbar() {
        supportActionBar?.title = "Solidity Learning"
        supportActionBar?.subtitle = "Master Smart Contract Development"
    }
    
    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewLevels)
        fabProgress = findViewById(R.id.fabProgress)
        
        fabProgress.setOnClickListener {
            startActivity(Intent(this, ProgressActivity::class.java))
        }
    }
    
    private fun setupRecyclerView() {
        dataManager = DataManager(this)
        levelAdapter = LevelAdapter { level ->
            if (level.isUnlocked) {
                val intent = Intent(this, LessonActivity::class.java)
                intent.putExtra("LEVEL_ID", level.id)
                startActivity(intent)
            }
        }
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = levelAdapter
        }
    }
    
    private fun loadLevels() {
        val levels = dataManager.getAllLevels()
        levelAdapter.updateLevels(levels)
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh levels in case progress has changed
        loadLevels()
    }
}
