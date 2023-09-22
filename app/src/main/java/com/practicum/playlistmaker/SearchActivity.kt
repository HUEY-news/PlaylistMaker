package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // РЕАКЦИЯ ПРИ НАЖАТИИ НА КНОПКУ "НАЗАД"
        val backButton = findViewById<ImageButton>(R.id.button_back)
        backButton.setOnClickListener { finish() }


    }
}