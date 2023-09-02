package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.button_search)
        searchButton.setOnClickListener{
            // TODO: переход к экрану SearchActivity
        }

        val libraryButton = findViewById<Button>(R.id.button_library)
        libraryButton.setOnClickListener {
            // TODO: переход к экрану LibraryActivity
        }

        val settingsButton: Button = findViewById(R.id.button_settings)
        val settingsButtonClickListener : View.OnClickListener = object : View.OnClickListener {
            override fun onClick(view: View?) {
                // TODO: переход к экрану SettingsActivity
            }
        }
        settingsButton.setOnClickListener(settingsButtonClickListener)
    }
}   