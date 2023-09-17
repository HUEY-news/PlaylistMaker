package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }

        val shareButton = findViewById<FrameLayout>(R.id.share_frame_layout)
        shareButton.setOnClickListener {
            val text = "https://practicum.yandex.ru/android-developer"
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(shareIntent)
        }

        val supportButton = findViewById<FrameLayout>(R.id.support_frame_layout)
        supportButton.setOnClickListener {
            val email = "HUEY.news@yandex.ru"
            val title = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val text = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            supportIntent.putExtra(Intent.EXTRA_TITLE, title)
            supportIntent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(supportIntent)
        }

        val agreementButton = findViewById<FrameLayout>(R.id.agreement_frame_layout)
        agreementButton.setOnClickListener {}
    }

//    val message = "Привет, Android-разработка — это круто!"
//    val shareIntent = Intent(Intent.ACTION_SENDTO)
//    shareIntent.data = Uri.parse("mailto:")
//    shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("yourEmail@ya.ru"))
//    shareIntent.putExtra(Intent.EXTRA_TEXT, message)
//    startActivity(shareIntent)
}