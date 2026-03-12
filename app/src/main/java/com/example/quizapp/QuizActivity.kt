package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

class QuizActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizScreen()
        }
    }

    @Composable
    fun QuizScreen() {
        val context = LocalContext.current
        val app = context.applicationContext as QuizApp
        val viewModel: QuizViewModel = viewModel(factory = ViewModelFactory(app.repository))

        val currentItem = viewModel.currentItem
        val options = viewModel.options
        val scrollState = rememberScrollState()

        if (viewModel.allItems.size < 3) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("You need at least 3 items to start the quiz", color = Color.Red)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { finish() }) { Text("Back") }
                }
            }
            return
        }

        if (currentItem == null) return

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Score: ${viewModel.correctScore} / ${viewModel.total}", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = currentItem.imageUri,
                    contentDescription = currentItem.name,
                    modifier = Modifier
                        .width(300.dp)
                        .height(180.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                options.forEachIndexed { index, item ->
                    val buttonColors = when {
                        viewModel.selectedItemIndex != -1 && item.id == currentItem.id -> ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        viewModel.selectedItemIndex == index && item.id != currentItem.id -> ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                        else -> ButtonDefaults.buttonColors()
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = viewModel.selectedItemIndex == -1,
                        colors = buttonColors,
                        onClick = { viewModel.submitAnswer(index) }
                    ) {
                        Text(item.name)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                viewModel.feedback?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(it, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.nextQuestion() }) { Text("Next") }
                }

                Spacer(modifier = Modifier.height(60.dp)) // Added space for the sticky button
            }

            Button(
                onClick = { finish() },
                modifier = Modifier.padding(16.dp).align(Alignment.BottomStart)
            ) { Text("Back to Main Menu") }
        }
    }
}
