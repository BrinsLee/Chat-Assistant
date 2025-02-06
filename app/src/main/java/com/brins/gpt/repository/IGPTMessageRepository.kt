package com.brins.gpt.repository

import com.brins.lib_base.model.GPTChatRequest
import com.brins.lib_base.model.GPTChatResponse
import com.brins.lib_base.model.audio.GPTTextToSpeechRequest
import com.brins.lib_base.model.vision.GPTChatRequestVision
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Message
import io.getstream.result.call.Call
import okhttp3.ResponseBody

interface IGPTMessageRepository {
    /**
     * 发送消息，未使用
     */
    suspend fun sendMessage(gptChatRequest: GPTChatRequest): GPTChatResponse?

    /**
     * 与gpt创建会话
     */
    suspend fun createCompletion(gptChatRequest: GPTChatRequest): GPTChatResponse?

    /**
     * 与gpt创建识图会话
     */
    suspend fun createCompletion(gptChatRequest: GPTChatRequestVision): GPTChatResponse?

    /**
     * 判断当前channel消息是否为空
     */
    suspend fun watchIsChannelMessageEmpty(cid: String, callback:(channel: Channel?) -> Unit)

    /**
     * 发送stream消息
     */
    suspend fun sendStreamMessage(cid: String, text: String, isFromMine: Boolean): Call<Message>

    /**
     * 发送stream消息
     */
    suspend fun sendStreamMessage(message: Message): Call<Message>



}