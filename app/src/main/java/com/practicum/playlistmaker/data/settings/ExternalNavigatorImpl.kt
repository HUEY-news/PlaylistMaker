package com.practicum.playlistmaker.data.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.practicum.playlistmaker.R

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {

    override fun shareLink() {
        val url = context.getString(R.string.YP_android_developer_link)
        val title = context.getString(R.string.share_app)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, url)
        val chooser = Intent.createChooser(intent, title)
        chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(context, chooser, null)
    }

    override fun openLink() {
        val url = context.getString(R.string.agreement_link)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(context, intent, null)
    }

    override fun openEmail() {

        val email = context.getString(R.string.support_email)
        val title = context.getString(R.string.write_to_user_support)
        val subject = context.getString(R.string.support_subject)
        val text = context.getString(R.string.support_text)
        val intent = Intent(Intent.ACTION_SEND)

        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)

        val chooser = Intent.createChooser(intent, title)
        chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(context, chooser, null)
    }
}