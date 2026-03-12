package com.example.quizapp

import kotlinx.coroutines.flow.Flow

class GalleryRepository(private val galleryDao: GalleryDao) {
    val allItems: Flow<List<GalleryItem>> = galleryDao.getAll()

    suspend fun insert(item: GalleryItem) {
        galleryDao.insert(item)
    }

    suspend fun delete(item: GalleryItem) {
        galleryDao.delete(item)
    }
    
    suspend fun getAllList(): List<GalleryItem> {
        return galleryDao.getAllList()
    }
}
