package com.brins.gpt.repository

import com.brins.lib_base.config.chatGPTUser
import com.brins.lib_base.model.GPTChatRequest
import com.brins.lib_base.model.GPTChatResponse
import com.brins.lib_network.service.IChatGPTService
import com.squareup.moshi.Moshi
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Message
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Response
import javax.inject.Inject
import io.getstream.result.call.Call
import java.util.UUID

class GPTMessageRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val chatClient: ChatClient,
    private val chatGptService: IChatGPTService
): IGPTMessageRepository {
    override suspend fun sendMessage(gptChatRequest: GPTChatRequest): Response<GPTChatResponse> {
        val moshi = Moshi.Builder().build()
//        val json = moshi.adapter(GPTChatRequest::class.java).toJson(gptChatRequest)
        val response = chatGptService.sendMessage(gptChatRequest)
        if (response.isSuccessful) {
            val gptChatResponse = response.body()
        }
        return response
    }

    override suspend fun createCompletion(gptChatRequest: GPTChatRequest): Response<GPTChatResponse> {
        val response = chatGptService.createCompletion(gptChatRequest)
        if (response.isSuccessful) {
            val gptChatResponse = response.body()
        }
        return response
    }

    override suspend fun watchIsChannelMessageEmpty(cid: String): Call<Channel> {
        return chatClient.channel(cid).watch()
    }

    override suspend fun sendStreamMessage(cid: String, text: String): Call<Message> {
        val channelClient = chatClient.channel(cid)
        return channelClient.sendMessage(
            Message(
                id = UUID.randomUUID().toString(),
                cid = cid,
                text = text,
                user = chatGPTUser,
                extraData = mutableMapOf("ChatGpt" to true)
            )
        )
    }
}