package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.button_back)
        backButton.setOnClickListener {
            finish()
        }

        // обработчик нажатия на кнопку "Поделиться приложением"
        val shareButton = findViewById<FrameLayout>(R.id.share_frame_layout)
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND) // создаем новый Intent с действием ACTION_SEND
            val shareIntentTitle = getString(R.string.share_app) // заголовок интента
            shareIntent.type = "text/plain" // устанавливаем тип данных для отправки
            val url = getString(R.string.yandex_practicum_android_developer_link) // создаём переменную для хранения ссылки на курс
            shareIntent.putExtra(Intent.EXTRA_TEXT, url) // добавляем данные для отправки (ссылку на курс)
            startActivity(Intent.createChooser(shareIntent, shareIntentTitle)) // запускаем Intent
        }

        // обработчик нажатия на кнопку "Написать в поддержку"
        val supportButton = findViewById<FrameLayout>(R.id.support_frame_layout)
        supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SEND) // создаем новый Intent с действием ACTION_SEND
            val supportIntentTitle = getString(R.string.write_to_user_support) // заголовок интента
            supportIntent.type = "message/rfc822" // устанавливаем тип данных для отправки
            val email = getString(R.string.support_email) // email-адрес студента, выполняющего задание
            val subject = getString(R.string.support_subject) // строка темы письма
            val text = getString(R.string.support_text) // текст в теле письма
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email)) // добавляем данные об email-адресе студента
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, subject) // указываем тему письма
            supportIntent.putExtra(Intent.EXTRA_TEXT, text) // добавляем текст письма для отправки
            startActivity(Intent.createChooser(supportIntent, supportIntentTitle)) // запускаем Intent
        }

        // обработчик нажатия на кнопку "Пользовательское соглашение"
        val agreementButton = findViewById<FrameLayout>(R.id.agreement_frame_layout)
        agreementButton.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW) // создаем новый Intent с действием ACTION_VIEW
            val url = getString(R.string.agreement_link) // создаём переменную для хранения ссылки на курс
            agreementIntent.data = Uri.parse(url) // передаём ссылку в качестве данных для интента
            startActivity(agreementIntent) // запускаем Intent
        }
    }
}