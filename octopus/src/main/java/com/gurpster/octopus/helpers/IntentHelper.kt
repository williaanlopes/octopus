package com.gurpster.octopus.helpers

import android.content.Intent
import android.net.Uri
import androidx.annotation.CheckResult

/**
 * The IntentCompanion functions are used internally.
 * They are placed in this object because only very few developers need them directly.
 * The usage is experimental because the object solution is not optimal.
 * It will be resolved as soon as statics are available in Kotlin.
 */
object IntentHelper {

    /**
     * Create an [Intent] to share plain text and/or an attachment.
     * How the individual parameters are interpreted depends on the application that is started with the intent.
     * @param subject The subject to send.
     * @param text The text to send.
     * @param attachment A content URI holding a stream of data to send.
     * @return The created intent.
     */
    @CheckResult
    fun createSendIntent(
        subject: String? = null,
        text: String? = null,
        attachment: Uri? = null
    ): Intent {
        val intent = Intent(Intent.ACTION_SEND).setType(MimeTypeHelper.PLAIN_TEXT)
        if (subject != null) intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (text != null) intent.putExtra(Intent.EXTRA_TEXT, text)
        if (attachment != null) intent.putExtra(Intent.EXTRA_STREAM, attachment)
        return intent
    }

    /**
     * Create an [Intent] to start a mail application.
     * @param subject The subject of the mail.
     * @param body The body of the mail.
     * @param recipient The recipient's mail address.
     * @param attachment A content URI holding a stream of data to send.
     * @return The created intent.
     */
    @CheckResult
    fun createMailSendIntent(
        subject: String? = null,
        body: String? = null,
        recipient: String? = null,
        attachment: Uri? = null
    ): Intent {
        val intent = Intent(Intent.ACTION_SEND).setType(MimeTypeHelper.MAIL_RFC822)
        if (recipient != null) intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        if (subject != null) intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (body != null) intent.putExtra(Intent.EXTRA_TEXT, body)
        if (attachment != null) intent.putExtra(Intent.EXTRA_STREAM, attachment)
        return intent
    }

}