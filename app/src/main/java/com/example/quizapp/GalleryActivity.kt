package com.example.quizapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

class GalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryScreen()
        }
    }

    @Composable
    fun GalleryScreen() {
        val context = LocalContext.current
        val app = context.applicationContext as QuizApp
        val viewModel: GalleryViewModel = viewModel(factory = ViewModelFactory(app.repository))
        val itemList by viewModel.allItems.collectAsState()

        var showDialog by remember { mutableStateOf(false) }
        var pendingImageUri by remember { mutableStateOf<Uri?>(null) }
        var flagName by remember { mutableStateOf("") }
        var sortAscending by remember { mutableStateOf(true) }

        val imagePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->
            uri?.let {
                try {
                    context.contentResolver.takePersistableUriPermission(
                        it,
                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) { }
                pendingImageUri = it
                showDialog = true
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    flagName = ""
                },
                title = { Text("Add flag") },
                text = {
                    TextField(
                        value = flagName,
                        onValueChange = { flagName = it },
                        label = { Text("Country") }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (pendingImageUri != null && flagName.isNotBlank()) {
                                viewModel.insert(
                                    GalleryItem(
                                        name = flagName,
                                        imageUri = pendingImageUri.toString()
                                    )
                                )
                            }
                            showDialog = false
                            flagName = ""
                            pendingImageUri = null
                        }
                    ) { Text("Add") }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            flagName = ""
                            pendingImageUri = null
                        }
                    ) { Text("Cancel") }
                }
            )
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { sortAscending = true }) { Text("A-Z") }
                Button(onClick = { sortAscending = false }) { Text("Z-A") }
            }

            val sortedList = if (sortAscending) {
                itemList.sortedBy { it.name.lowercase() }
            } else {
                itemList.sortedByDescending { it.name.lowercase() }
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(sortedList) { item ->
                    GalleryRow(item) { toRemove ->
                        viewModel.delete(toRemove)
                    }
                }
            }

            if (itemList.size < 3) {
                Text(
                    "Cannot start Quiz: at least 3 items required.",
                    fontSize = 16.sp,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Button(onClick = { imagePicker.launch(arrayOf("image/*")) }) {
                Text("Add new entry")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { finish() }, modifier = Modifier.fillMaxWidth()) {
                Text("Back to Main Menu")
            }
        }
    }

    @Composable
    fun GalleryRow(item: GalleryItem, onRemove: (GalleryItem) -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            AsyncImage(
                model = item.imageUri,
                contentDescription = item.name,
                modifier = Modifier
                    .width(150.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = item.name, fontSize = 20.sp, modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove item",
                modifier = Modifier.size(30.dp).clickable { onRemove(item) }
            )
        }
    }
}
