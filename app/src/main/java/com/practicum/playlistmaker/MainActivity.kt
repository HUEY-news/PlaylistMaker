package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch: Button = findViewById(R.id.button_search)
        val buttonSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(view: View?) {
                Toast.makeText(
                    this@MainActivity,
                    "Нажата кнопка \"Поиск\"",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        buttonSearch.setOnClickListener(buttonSearchClickListener)

        val buttonLibrary: Button = findViewById(R.id.button_library)
        buttonLibrary.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                "Нажата кнопка \"Медиатека\"",
                Toast.LENGTH_SHORT
            ).show()
        }

        val buttonSettings: Button = findViewById(R.id.button_settings)
        buttonSettings.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                "Нажата кнопка \"Настройки\"",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}   