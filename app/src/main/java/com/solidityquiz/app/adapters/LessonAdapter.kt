package com.solidityquiz.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.solidityquiz.app.R
import com.solidityquiz.app.models.Lesson
import io.github.kbiakov.codeview.CodeView
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme

class LessonAdapter(
    private val onLessonClick: (Lesson) -> Unit
) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {
    
    private var lessons = listOf<Lesson>()
    private var currentLessonIndex = 0
    
    fun updateLessons(newLessons: List<Lesson>) {
        lessons = newLessons
        notifyDataSetChanged()
    }
    
    fun setCurrentLesson(index: Int) {
        val oldIndex = currentLessonIndex
        currentLessonIndex = index
        notifyItemChanged(oldIndex)
        notifyItemChanged(currentLessonIndex)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return LessonViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(lessons[position], position == currentLessonIndex)
    }
    
    override fun getItemCount(): Int = lessons.size
    
    inner class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.cardLesson)
        private val titleTextView: TextView = itemView.findViewById(R.id.tvLessonTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tvLessonDescription)
        private val explanationTextView: TextView = itemView.findViewById(R.id.tvExplanation)
        private val codeView: CodeView = itemView.findViewById(R.id.codeView)
        
        fun bind(lesson: Lesson, isCurrentLesson: Boolean) {
            titleTextView.text = lesson.title
            descriptionTextView.text = lesson.description
            explanationTextView.text = lesson.explanation
            
            // Setup code view with syntax highlighting
            codeView.setOptions(Options.Default.get(itemView.context)
                .withLanguage("solidity")
                .withTheme(ColorTheme.MONOKAI)
                .withCode(lesson.codeContent)
                .withFont("monospace"))
            
            // Highlight current lesson
            if (isCurrentLesson) {
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.primary_light)
                )
                cardView.strokeColor = ContextCompat.getColor(itemView.context, R.color.primary_blue)
                cardView.strokeWidth = 4
            } else {
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.white)
                )
                cardView.strokeColor = ContextCompat.getColor(itemView.context, R.color.light_gray)
                cardView.strokeWidth = 1
            }
            
            // Mark completed lessons
            if (lesson.isCompleted) {
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_check_circle, 0
                )
            } else {
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
            
            cardView.setOnClickListener {
                onLessonClick(lesson)
            }
        }
    }
}
