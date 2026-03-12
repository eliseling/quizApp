package com.example.quizapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GalleryDao {
    @Query("SELECT * FROM gallery_items")
    fun getAll(): Flow<List<GalleryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GalleryItem)

    @Delete
    suspend fun delete(item: GalleryItem)
    
    @Query("SELECT * FROM gallery_items")
    suspend fun getAllList(): List<GalleryItem>
}
