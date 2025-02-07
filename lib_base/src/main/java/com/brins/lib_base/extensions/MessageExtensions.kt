package com.brins.lib_base.extensions

import androidx.core.content.ContextCompat
import com.brins.lib_base.R
import com.brins.lib_base.config.GPT_MESSAGE_KEY
import com.brins.lib_base.config.ROLE_USER
import com.brins.lib_base.model.GPTChatResponse
import com.brins.lib_base.model.GPTMessage
import com.brins.lib_base.model.image.GPTImageResponse
import com.brins.lib_base.model.vision.CONTENT_TYPE_IMAGE
import com.brins.lib_base.model.vision.CONTENT_TYPE_TEXT
import com.brins.lib_base.model.vision.DETAIL_LEVEL_HIGH
import com.brins.lib_base.model.vision.GPTContent
import com.brins.lib_base.model.vision.GPTImageUrl
import com.brins.lib_base.model.vision.GPTMessageVision
import com.brins.lib_base.utils.AppUtils
import io.getstream.chat.android.models.Attachment
import io.getstream.chat.android.models.Message

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
    return GPTMessage(ROLE_USER, this.text, "")
}

fun Message.toGPTMessageVision(): GPTMessageVision {
    val prompt =
        if (this.text.isNotEmpty()) this.text else if (this.attachments.size > 1) ContextCompat.getString(
            AppUtils.sApplication!!.applicationContext, R.string.default_vision_prompts
        )
        else ContextCompat.getString(
            AppUtils.sApplication!!.applicationContext, R.string.default_vision_prompt
        )
    val gptContentText: GPTContent = GPTContent(CONTENT_TYPE_TEXT, prompt)
    val gptContentImages: List<GPTContent> = this.attachments.map { attachment ->
        GPTContent(
            CONTENT_TYPE_IMAGE, imageUrl = GPTImageUrl(attachment.imageUrl ?: "", DETAIL_LEVEL_HIGH)
        )
    }
    val messageList: MutableList<GPTContent> = mutableListOf()
    messageList.add(gptContentText)
    messageList.addAll(gptContentImages)
    return GPTMessageVision(ROLE_USER, messageList)
}


fun GPTChatResponse.toMessage(cid: String): Message {
    val id = this.id
    val model = this.model
    val choice = this.choices
    return Message(id, cid, choice[0].message?.content ?: "")
}

fun GPTChatResponse.toMessage(message: Message): Message {
    val id = this.id
    val model = this.model
    val choice = this.choices
    val extraData: MutableMap<String, Any> = mutableMapOf(GPT_MESSAGE_KEY to true)
    extraData.putAll(message.extraData)
    return Message.Builder().withId(id).withCid(message.cid)
        .withText(choice[0].message?.content ?: "").withExtraData(extraData).build()
}

fun GPTImageResponse.toMessage(message: Message): Message {
    val id = "${message.id}-${this.created}"
    val imageData = data[0]
    val extraData: MutableMap<String, Any> = mutableMapOf(GPT_MESSAGE_KEY to true)
    extraData.putAll(message.extraData)
    return Message.Builder().withId(id).withCid(message.cid).withText(imageData.revisedPrompt)
        .withExtraData(extraData).withAttachments(
            listOf(
                Attachment(
                    authorName = "DALL-E",
                    imageUrl = imageData.url,
                    mimeType = "png",
                    originalHeight = 1024,
                    originalWidth = 1024,
                    type = "image"
                )
            )
        ).build()
}