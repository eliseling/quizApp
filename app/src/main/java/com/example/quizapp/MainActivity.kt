package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Kobler aktiviteten til XML-layouten i res/layout/activity_main.xml [7, 8]
        setContentView(R.layout.activity_main)

        // Henter referanser til knappene fra XML ved hjelp av deres ID [9, 10]
        val galleryBtn = findViewById<Button>(R.id.buttonGallery)
        val quizBtn = findViewById<Button>(R.id.buttonQuiz)

        // Lytter etter klikk for å navigere til Galleri-skjermen [11, 12]
        galleryBtn.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }

        // Lytter etter klikk for å starte Quizen [11]
        quizBtn.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }
    }
}