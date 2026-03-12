package com.example.quizapp

import android.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: GalleryRepository) : ViewModel() {
    var allItems by mutableStateOf<List<GalleryItem>>(emptyList())
        private set

    var currentItem by mutableStateOf<GalleryItem?>(null)
        private set

    var options by mutableStateOf<List<GalleryItem>>(emptyList())
        private set

    var total by mutableIntStateOf(0)
    var correctScore by mutableIntStateOf(0)
    var feedback by mutableStateOf<String?>(null)
    var selectedItemIndex by mutableIntStateOf(-1)

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            allItems = repository.getAllList()
            if (allItems.size >= 3 && currentItem == null) {
                nextQuestion()
            }
        }
    }

    fun nextQuestion() {
        if (allItems.size < 3) return
        
        val nextItem = if (currentItem == null) {
            allItems.random()
        } else {
            val remaining = allItems.filter { it.id != currentItem?.id }
            remaining.random()
        }
        
        currentItem = nextItem
        val wrong = allItems.filter { it.id != nextItem.id }.shuffled().take(2)
        options = (wrong + nextItem).shuffled()
        
        feedback = null
        selectedItemIndex = -1
    }

    fun submitAnswer(index: Int) {
        if (selectedItemIndex != -1 || currentItem == null) return
        
        selectedItemIndex = index
        total++
        val selectedItem = options[index]
        if (selectedItem.id == currentItem?.id) {
            correctScore++
            feedback = "Correct!"

        } else {
            feedback = "Wrong! Correct answer: ${currentItem?.name}"
        }
    }
}
