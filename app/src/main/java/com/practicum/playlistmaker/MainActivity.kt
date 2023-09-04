package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.button_search)
        searchButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }

        val libraryButton = findViewById<Button>(R.id.button_library)
        libraryButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, LibraryActivity::class.java))
        }

        val settingsButton: Button = findViewById(R.id.button_settings)
        settingsButton.setOnClickListener() {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }
    }
}   