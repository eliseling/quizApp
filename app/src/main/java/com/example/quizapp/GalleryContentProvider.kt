package com.example.quizapp

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import kotlinx.coroutines.runBlocking

class GalleryContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.quizapp.provider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/gallery_items")
    }

    private lateinit var database: AppDatabase

    override fun onCreate(): Boolean {
        database = AppDatabase.getDatabase(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val cursor = MatrixCursor(arrayOf("name", "URI"))
        runBlocking {
            val items = database.galleryDao().getAllList()
            items.forEach {
                cursor.addRow(arrayOf(it.name, it.imageUri))
            }
        }
        return cursor
    }

    override fun getType(uri: Uri): String? = "vnd.android.cursor.dir/$AUTHORITY.gallery_items"

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
