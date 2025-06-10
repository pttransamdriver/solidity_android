package com.solidityquiz.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.solidityquiz.app.R
import com.solidityquiz.app.models.Level

class ProgressAdapter : RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>() {
    
    private var levels = listOf<Level>()
    
    fun updateLevels(newLevels: List<Level>) {
        levels = newLevels
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progress, parent, false)
        return ProgressViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        holder.bind(levels[position])
    }
    
    override fun getItemCount(): Int = levels.size
    
    inner class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImageView: ImageView = itemView.findViewById(R.id.ivProgressIcon)
        private val titleTextView: TextView = itemView.findViewById(R.id.tvProgressTitle)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBarLevel)
        private val progressTextView: TextView = itemView.findViewById(R.id.tvProgressPercentage)
        private val statusTextView: TextView = itemView.findViewById(R.id.tvProgressStatus)
        private val lessonsTextView: TextView = itemView.findViewById(R.id.tvLessonsProgress)
        
        fun bind(level: Level) {
            iconImageView.setImageResource(level.iconResource)
            titleTextView.text = level.title
            progressBar.progress = level.progress
            progressTextView.text = "${level.progress}%"
            
            // Calculate completed lessons
            val totalLessons = level.lessons.size
            val completedLessons = (level.progress * totalLessons) / 100
            lessonsTextView.text = "Lessons: $completedLessons/$totalLessons"
            
            // Set status
            when {
                level.isCompleted -> {
                    statusTextView.text = "✓ Completed"
                    statusTextView.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.success_green)
                    )
                }
                level.isUnlocked && level.progress > 0 -> {
                    statusTextView.text = "In Progress"
                    statusTextView.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.warning_orange)
                    )
                }
                level.isUnlocked -> {
                    statusTextView.text = "Available"
                    statusTextView.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.primary_blue)
                    )
                }
                else -> {
                    statusTextView.text = "🔒 Locked"
                    statusTextView.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.disabled_gray)
                    )
                }
            }
        }
    }
}
