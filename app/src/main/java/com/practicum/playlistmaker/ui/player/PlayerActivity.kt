package com.practicum.playlistmaker.ui.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.practicum.playlistmaker.util.Creator

class PlayerActivity : AppCompatActivity() {

    private val playerController = Creator.providePlayerController (
        this,
        lifecycleScope
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerController.onCreate()
    }

    override fun onPause() {
        super.onPause()
        playerController.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerController.onDestroy()

    }
}
