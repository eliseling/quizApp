package com.example.quizapp

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class QuizApp : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { GalleryRepository(database.galleryDao()) }
    
    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        
        // Populate database with initial data if it's empty
        applicationScope.launch {
            val items = repository.getAllList()
            if (items.isEmpty()) {
                val package_name = packageName
                val initialItems = listOf(
                    GalleryItem(name = "China", imageUri = "android.resource://$package_name/${R.drawable.china}"),
                    GalleryItem(name = "Namibia", imageUri = "android.resource://$package_name/${R.drawable.namibia}"),
                    GalleryItem(name = "Bhutan", imageUri = "android.resource://$package_name/${R.drawable.bhutan}"),
                    GalleryItem(name = "Brazil", imageUri = "android.resource://$package_name/${R.drawable.brazil}"),
                    GalleryItem(name = "Italy", imageUri = "android.resource://$package_name/${R.drawable.italy}")
                )
                initialItems.forEach { repository.insert(it) }
            }
        }
    }
}
