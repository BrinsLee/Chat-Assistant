package com.brins.lib_base.utils

import com.brins.lib_base.extensions.isChatGPT
import com.brins.lib_base.extensions.isSameMessage
import io.getstream.chat.android.client.debugger.ChatClientDebugger
import io.getstream.chat.android.client.debugger.SendMessageDebugger
import io.getstream.chat.android.models.Message
import io.getstream.log.taggedLogger
import io.getstream.result.Error
import io.getstream.result.Result
import javax.inject.Inject
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

class CustomChatClientDebugger @Inject constructor() : ChatClientDebugger {

    override fun onNonFatalErrorOccurred(tag: String, src: String, desc: String, error: Error) {

    }

    override fun debugSendMessage(
        channelType: String,
        channelId: String,
        message: Message,
        isRetrying: Boolean,
    ): SendMessageDebugger {
        return CustomSendMessageDebugger(channelType, channelId, message, isRetrying)
    }
}

class CustomSendMessageDebugger(
    channelType: String,
    channelId: String,
    message: Message,
    isRetrying: Boolean,
) : SendMessageDebugger {

    private val logger by taggedLogger("SendMessageDebugger")

    private val cid = "$channelType:$channelId"

    private var mSendingMessage: Message? = null

    init {

        logger.i { "<init> #debug; isRetrying: $isRetrying, cid: $cid, message: $message" }
    }

    override fun onStart(message: Message) {
        logger.d { "[onStart] #debug; message: $message" }
    }

    override fun onInterceptionStart(message: Message) {
        logger.d { "[onInterceptionStart] #debug; message: $message" }
        mSendingMessage = message
    }

    override fun onInterceptionUpdate(message: Message) {
        logger.d { "[onInterceptionUpdate] #debug; message: $message" }
        mSendingMessage?.let {
            if (it.user.isChatGPT() && it.isSameMessage(message)) {
                tryModifyMessageUser(it, message)
            }
        }
    }

    override fun onInterceptionStop(result: Result<Message>, message: Message) {
        logger.v { "[onInterceptionStop] #debug; result: $result, message: $message" }
        mSendingMessage = null
    }

    override fun onSendStart(message: Message) {
        logger.d { "[onSendStart] #debug; message: $message" }
    }

    override fun onSendStop(result: Result<Message>, message: Message) {
        logger.v { "[onSendStop] #debug; result: $result, message: $message" }
    }

    override fun onStop(result: Result<Message>, message: Message) {
        logger.v { "[onStop] #debug; result: $result, message: $message" }
    }

    private fun tryModifyMessageUser(sourceMessage: Message, targetMessage: Message) {
        val readOnlyProperty = Message::class.memberProperties
            .first { it.name == "user" }
            .javaField?.apply { isAccessible = true }
        readOnlyProperty?.set(targetMessage, sourceMessage.user)
    }
}