package com.example.quizapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gallery_items")
data class GalleryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageUri: String // Using String to store URI as suggested
)
