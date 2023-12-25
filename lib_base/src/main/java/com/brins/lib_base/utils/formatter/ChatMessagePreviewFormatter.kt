package com.brins.lib_base.utils.formatter

import android.text.SpannableString
import android.text.SpannableStringBuilder
import com.brins.lib_base.extensions.getSenderDisplayNames
import io.getstream.chat.android.client.utils.message.isSystem
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Message
import io.getstream.chat.android.models.User
import io.getstream.chat.android.ui.common.utils.extensions.isDirectMessaging
import io.getstream.chat.android.ui.helper.MessagePreviewFormatter

class ChatMessagePreviewFormatter: MessagePreviewFormatter {
    override fun formatMessagePreview(
        channel: Channel, message: Message, currentUser: User?
    ): CharSequence {
        if (message.isSystem()) {
            return SpannableStringBuilder(message.text.trim())
        } else {
            val sender = message.getSenderDisplayNames(channel.isDirectMessaging())
            // bold mentions of the current user
            val previewText: SpannableString = SpannableString(message.text.trim())
//                val attachmentsText: SpannableString? = message.getAttachmentsText()
            return listOf(sender, previewText)
                .filterNot { it.isNullOrEmpty() }
                .joinTo(SpannableStringBuilder(), ": ")
        }
    }
}