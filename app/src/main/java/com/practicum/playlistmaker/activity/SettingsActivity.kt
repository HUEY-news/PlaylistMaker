package com.practicum.playlistmaker.activity

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val nightThemeSwitcher = findViewById<SwitchCompat>(R.id.switch_night_theme)
        // ЕСЛИ ПРИ ЗАПУСКЕ НОЧНАЯ ТЕМА УЖЕ АКТИВНА...
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK === Configuration.UI_MODE_NIGHT_YES)
        {
            nightThemeSwitcher.isChecked = true
        }
        nightThemeSwitcher.setOnClickListener {
            // МЕТОД setDefaultNightMode() КЛАССА AppCompatDelegate УСТАНАВЛИВАЕТ НОЧНУЮ ТЕМУ ДЛЯ ВСЕГО ПРИЛОЖЕНИЯ...
            if (nightThemeSwitcher.isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            // ПЕРЕСОЗДАТЬ ACTIVITY С НОВОЙ ТЕМОЙ...
            recreate()
        }
        // ВАЖНО!!!
        // Частое переключение тем может вызвать утечку памяти, если необходимые ресурсы не будут корректно освобождены.
        // Однако, если приложение правильно управляет своими ресурсами и использует метод recreate() только при изменении темы,
        // то утечек памяти не должно возникать.

        val backButton = findViewById<ImageView>(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }

        val shareButton = findViewById<FrameLayout>(R.id.fl_share)
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            val shareIntentTitle = getString(R.string.share_app)
            shareIntent.type = "text/plain"
            val url = getString(R.string.YP_android_developer_link)
            shareIntent.putExtra(Intent.EXTRA_TEXT, url)
            startActivity(Intent.createChooser(shareIntent, shareIntentTitle))
        }

        val supportButton = findViewById<FrameLayout>(R.id.fl_support)
        supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SEND)
            val supportIntentTitle = getString(R.string.write_to_user_support)
            supportIntent.type = "message/rfc822"
            val email = getString(R.string.support_email)
            val subject = getString(R.string.support_subject)
            val text = getString(R.string.support_text)
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            supportIntent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(supportIntent, supportIntentTitle))
        }

        val agreementButton = findViewById<FrameLayout>(R.id.fl_agreement)
        agreementButton.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            val url = getString(R.string.agreement_link)
            agreementIntent.data = Uri.parse(url)
            startActivity(agreementIntent)
        }
    }
}