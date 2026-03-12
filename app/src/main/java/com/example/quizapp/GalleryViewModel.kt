package com.example.quizapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GalleryViewModel(private val repository: GalleryRepository) : ViewModel() {

    val allItems: StateFlow<List<GalleryItem>> = repository.allItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insert(item: GalleryItem) {
        viewModelScope.launch {
            repository.insert(item)
        }
    }

    fun delete(item: GalleryItem) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }
}
