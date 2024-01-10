package com.practicum.playlistmaker.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.utils.isUsingNightModeResources

class SettingsActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        // реализация переключения темы:
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = isUsingNightModeResources
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        // реализация кнопки "назад":
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        // реализация кнопки "поделиться приложением":
        val shareButton = findViewById<FrameLayout>(R.id.shareContainer)
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            val shareIntentTitle = getString(R.string.share_app)
            shareIntent.type = "text/plain"
            val url = getString(R.string.YP_android_developer_link)
            shareIntent.putExtra(Intent.EXTRA_TEXT, url)
            startActivity(Intent.createChooser(shareIntent, shareIntentTitle))
        }

        // реализация кнопки "написать в поддержку":
        val supportButton = findViewById<FrameLayout>(R.id.supportContainer)
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

        // реализация кнопки "пользовательское соглашение":
        val agreementButton = findViewById<FrameLayout>(R.id.agreementContainer)
        agreementButton.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            val url = getString(R.string.agreement_link)
            agreementIntent.data = Uri.parse(url)
            startActivity(agreementIntent)
        }
    }
}