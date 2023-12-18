package com.brins.gpt.repository

import com.brins.lib_base.model.GPTChatRequest
import com.brins.lib_base.model.GPTChatResponse
import retrofit2.Response

interface IGPTMessageRepository {
    suspend fun sendMessage(gptChatRequest: GPTChatRequest): Response<GPTChatResponse>

    suspend fun createCompletion(gptChatRequest: GPTChatRequest): Response<GPTChatResponse>
}