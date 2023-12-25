package com.brins.lib_base.extensions

import android.content.Context
import com.brins.lib_base.config.GPT_MESSAGE_KEY
import com.brins.lib_base.config.ROLE_USER
import com.brins.lib_base.model.GPTChatResponse
import com.brins.lib_base.model.GPTMessage
import io.getstream.chat.android.models.Message
import io.getstream.chat.android.ui.R
import java.util.UUID

fun Message.isSameMessage(message: Message): Boolean {
    return this.identifierHash() == message.identifierHash()
}

fun Message.isFromChatGPT(): Boolean {
    return this.user.isChatGPT() || (extraData.containsKey(GPT_MESSAGE_KEY) && extraData[GPT_MESSAGE_KEY] == true)
}

fun Message.getSenderDisplayNames(isDirectMessaging: Boolean = false): String? = when {
    isFromChatGPT() -> "GPT"
    isDirectMessaging -> null
    else -> ""
}

fun Message.toGPTMessage(): GPTMessage {
    return GPTMessage( ROLE_USER, this.text)
}

fun GPTChatResponse.toMessage(cid: String): Message {
    val id = this.id
    val model = this.model
    val choice = this.choices
    return Message(id, cid, choice[0].message?.content?:"")
}