package com.practicum.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.library.LibraryActivity
import com.practicum.playlistmaker.ui.search.activity.SearchActivity
import com.practicum.playlistmaker.ui.settings.SettingsActivity

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