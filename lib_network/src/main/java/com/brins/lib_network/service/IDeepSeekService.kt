package com.brins.lib_network.service

import com.brins.lib_base.model.GPTChatRequest
import com.brins.lib_base.model.GPTChatResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @description
 * @author lipeilin
 * @date 2025/2/6
 */
interface IDeepSeekService {

    /**
     * 普通聊天接口
     */
    @POST("chat/completions")
    suspend fun createCompletion(@Body body: GPTChatRequest): Response<GPTChatResponse>
}