package com.brins.gpt.repository

import com.brins.lib_base.model.audio.GPTTextToSpeechRequest
import com.brins.lib_network.service.IChatGPTService
import com.brins.lib_network.utils.NetworkUtils
import io.getstream.chat.android.client.ChatClient
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * @author lipeilin
 * @date 2024/4/4
 * @desc
 */
class GPTAudioRepositoryImpl@Inject constructor(
    private val networkUtils: NetworkUtils,
    private val chatClient: ChatClient,
    private val chatGptService: IChatGPTService
) : IGPTAudioRepository  {
    override suspend fun messageTextToSpeech(gptTextToSpeechRequest: GPTTextToSpeechRequest): ResponseBody? {
        val result = networkUtils.safeApiCall {
            chatGptService.createSpeech(gptTextToSpeechRequest)
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
}