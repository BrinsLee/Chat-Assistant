package com.brins.gpt.repository

import com.brins.lib_base.config.GPT_MESSAGE_KEY
import com.brins.lib_base.config.chatGPTUser
import com.brins.lib_base.model.GPTChatRequest
import com.brins.lib_base.model.GPTChatResponse
import com.brins.lib_base.model.audio.GPTTextToSpeechRequest
import com.brins.lib_base.model.vision.GPTChatRequestVision
import com.brins.lib_network.service.IChatGPTService
import com.brins.lib_network.utils.NetworkUtils
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Message
import javax.inject.Inject
import io.getstream.result.call.Call
import io.getstream.result.onErrorSuspend
import io.getstream.result.onSuccessSuspend
import okhttp3.ResponseBody
import java.util.UUID

class GPTMessageRepositoryImpl @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val chatClient: ChatClient,
    private val chatGptService: IChatGPTService
) : IGPTMessageRepository {

    private var currentChannel: Channel? = null

    override suspend fun sendMessage(gptChatRequest: GPTChatRequest): GPTChatResponse? {/*val moshi = Moshi.Builder().build()
//        val json = moshi.adapter(GPTChatRequest::class.java).toJson(gptChatRequest)
        val response = chatGptService.sendMessage(gptChatRequest)
        if (response.isSuccessful) {
            val gptChatResponse = response.body()
        }
        return response*/
        val result = networkUtils.safeApiCall { chatGptService.sendMessage(gptChatRequest) }
        when (result) {
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
        return when (result) {
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
        return when (result) {
            is NetworkUtils.Result.Success -> {
                result.data
            }

            is NetworkUtils.Result.Error -> {
                null
            }
        }
    }

    override suspend fun watchIsChannelMessageEmpty(
        cid: String,
        callback: (channel: Channel?) -> Unit
    ) {
        chatClient.channel(cid).watch().await().onSuccessSuspend { channel ->
            currentChannel = channel
            callback.invoke(channel)
        }.onErrorSuspend {
            currentChannel = null
            callback.invoke(null)
        }
    }

    override suspend fun sendStreamMessage(
        cid: String,
        text: String,
        isFromMine: Boolean
    ): Call<Message> {
        val channelClient = chatClient.channel(cid)
        val extraData = mutableMapOf<String, Any>()
        currentChannel?.extraData?.let {
            extraData.putAll(it)
        }
        if (!isFromMine) {
            extraData[GPT_MESSAGE_KEY] = true
        }
        return channelClient.sendMessage(
            Message(
                id = UUID.randomUUID().toString(),
                cid = cid,
                text = text,
                user = if (isFromMine) chatClient.getCurrentUser()!! else chatGPTUser,
                extraData = extraData
            )
        )
    }

/*    override suspend fun sendStreamMessage(message: Message, isFromMine: Boolean): Call<Message> {
        return if (isFromMine) {
            val channelClient = chatClient.channel(message.cid)
            channelClient.sendMessage(message)
        } else {
            sendStreamMessage(message)
        }

    }*/

    override suspend fun sendStreamMessage(message: Message): Call<Message> {
        val channelClient = chatClient.channel(message.cid)
        val realSendMessage = message.copy(user = chatGPTUser)
        return channelClient.sendMessage(realSendMessage)
    }


}