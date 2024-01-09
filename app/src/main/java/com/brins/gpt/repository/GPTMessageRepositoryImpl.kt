package com.brins.gpt.repository

import com.brins.lib_base.config.GPT_MESSAGE_KEY
import com.brins.lib_base.config.chatGPTUser
import com.brins.lib_base.model.GPTChatRequest
import com.brins.lib_base.model.GPTChatResponse
import com.brins.lib_base.model.vision.GPTChatRequestVision
import com.brins.lib_network.service.IChatGPTService
import com.brins.lib_network.utils.NetworkUtils
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
    private val networkUtils: NetworkUtils,
    private val chatClient: ChatClient,
    private val chatGptService: IChatGPTService
): IGPTMessageRepository {
    override suspend fun sendMessage(gptChatRequest: GPTChatRequest): GPTChatResponse? {
        /*val moshi = Moshi.Builder().build()
//        val json = moshi.adapter(GPTChatRequest::class.java).toJson(gptChatRequest)
        val response = chatGptService.sendMessage(gptChatRequest)
        if (response.isSuccessful) {
            val gptChatResponse = response.body()
        }
        return response*/
        val result = networkUtils.safeApiCall { chatGptService.sendMessage(gptChatRequest) }
        when(result) {
            is NetworkUtils.Result.Success -> {
                return result.data
            }

            is NetworkUtils.Result.Error -> {
                return null
            }
        }
    }

    override suspend fun createCompletion(gptChatRequest: GPTChatRequest): GPTChatResponse? {
        val result = networkUtils.safeApiCall {
            chatGptService.createCompletion(gptChatRequest)
        }
        return when(result) {
            is NetworkUtils.Result.Success -> {
                result.data
            }

            is NetworkUtils.Result.Error -> {
                null
            }
        }
    }

    override suspend fun createCompletion(gptChatRequest: GPTChatRequestVision): GPTChatResponse? {
        val result = networkUtils.safeApiCall {
            chatGptService.createCompletion(gptChatRequest)
        }
        return when(result) {
            is NetworkUtils.Result.Success -> {
                result.data
            }
            is NetworkUtils.Result.Error -> {
                null
            }
        }
    }

    override suspend fun watchIsChannelMessageEmpty(cid: String): Call<Channel> {
        return chatClient.channel(cid).watch()
    }

    override suspend fun sendStreamMessage(cid: String, text: String, isFromMine: Boolean): Call<Message> {
        val channelClient = chatClient.channel(cid)
        return channelClient.sendMessage(
            Message(
                id = UUID.randomUUID().toString(),
                cid = cid,
                text = text,
                user = if (isFromMine) chatClient.getCurrentUser()!! else chatGPTUser,
                extraData = if (isFromMine) emptyMap() else mutableMapOf(GPT_MESSAGE_KEY to true)
            )
        )
    }

    override suspend fun sendStreamMessage(message: Message, isFromMine: Boolean): Call<Message> {
        return if (isFromMine) {
            val channelClient = chatClient.channel(message.cid)
            channelClient.sendMessage(message)
        } else {
            sendStreamMessage(message)
        }

    }

    override suspend fun sendStreamMessage(message: Message): Call<Message> {
        val channelClient = chatClient.channel(message.cid)
        val realSendMessage = message.copy(user = chatGPTUser, extraData = mutableMapOf(GPT_MESSAGE_KEY to true))
        return channelClient.sendMessage(realSendMessage)
    }
}