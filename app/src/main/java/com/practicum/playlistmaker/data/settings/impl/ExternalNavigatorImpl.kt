package com.practicum.playlistmaker.data.settings.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.settings.api.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {

    override fun shareLink() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        val shareIntentTitle = context.getString(R.string.share_app)
        shareIntent.type = "text/plain"
        val url = context.getString(R.string.YP_android_developer_link)
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        context.startActivity(Intent.createChooser(shareIntent, shareIntentTitle))
    }

    override fun openLink() {
        val agreementIntent = Intent(Intent.ACTION_VIEW)
        val url = context.getString(R.string.agreement_link)
        agreementIntent.data = Uri.parse(url)
        context.startActivity(agreementIntent)
    }

    override fun openEmail() {
        val supportIntent = Intent(Intent.ACTION_SEND)
        val supportIntentTitle = context.getString(R.string.write_to_user_support)
        supportIntent.type = "message/rfc822"
        val email = context.getString(R.string.support_email)
        val subject = context.getString(R.string.support_subject)
        val text = context.getString(R.string.support_text)
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        supportIntent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(supportIntent, supportIntentTitle))
    }
}