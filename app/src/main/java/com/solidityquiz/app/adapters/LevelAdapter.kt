package com.solidityquiz.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.solidityquiz.app.R
import com.solidityquiz.app.models.Level

class LevelAdapter(
    private val onLevelClick: (Level) -> Unit
) : RecyclerView.Adapter<LevelAdapter.LevelViewHolder>() {
    
    private var levels = listOf<Level>()
    
    fun updateLevels(newLevels: List<Level>) {
        levels = newLevels
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_level, parent, false)
        return LevelViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(levels[position])
    }
    
    override fun getItemCount(): Int = levels.size
    
    inner class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.cardLevel)
        private val iconImageView: ImageView = itemView.findViewById(R.id.ivLevelIcon)
        private val titleTextView: TextView = itemView.findViewById(R.id.tvLevelTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tvLevelDescription)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressLevel)
        private val progressTextView: TextView = itemView.findViewById(R.id.tvProgress)
        private val statusTextView: TextView = itemView.findViewById(R.id.tvStatus)
        
        fun bind(level: Level) {
            titleTextView.text = level.title
            descriptionTextView.text = level.description
            iconImageView.setImageResource(level.iconResource)
            
            // Set progress
            progressBar.progress = level.progress
            progressTextView.text = "${level.progress}%"
            
            // Set status and styling
            when {
                level.isCompleted -> {
                    statusTextView.text = "Completed"
                    statusTextView.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.success_green)
                    )
                    cardView.alpha = 1.0f
                }
                level.isUnlocked -> {
                    statusTextView.text = "Available"
                    statusTextView.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.primary_blue)
                    )
                    cardView.alpha = 1.0f
                }
                else -> {
                    statusTextView.text = "Locked"
                    statusTextView.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.disabled_gray)
                    )
                    cardView.alpha = 0.6f
                }
            }
            
            // Set click listener
            cardView.setOnClickListener {
                onLevelClick(level)
            }
        }
    }
}
