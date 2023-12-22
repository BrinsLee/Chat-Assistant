package com.brins.gpt.repository

import com.brins.lib_base.model.GPTChatRequest
import com.brins.lib_base.model.GPTChatResponse
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Message
import io.getstream.result.call.Call
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IGPTMessageRepository {
    suspend fun sendMessage(gptChatRequest: GPTChatRequest): Response<GPTChatResponse>

    suspend fun createCompletion(gptChatRequest: GPTChatRequest): Response<GPTChatResponse>

    suspend fun watchIsChannelMessageEmpty(cid: String): Call<Channel>

    suspend fun sendStreamMessage(cid: String, text: String): Call<Message>
}