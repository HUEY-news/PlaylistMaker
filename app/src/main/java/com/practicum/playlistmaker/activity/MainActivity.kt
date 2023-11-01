package com.practicum.playlistmaker.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.practicum.playlistmaker.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }

        val libraryButton = findViewById<Button>(R.id.libraryButton)
        libraryButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, LibraryActivity::class.java))
        }

        val settingsButton: Button = findViewById(R.id.settingsButton)
        settingsButton.setOnClickListener() {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }
    }
}   