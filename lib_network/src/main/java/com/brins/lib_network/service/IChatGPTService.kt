
package com.brins.lib_network.service

import com.brins.lib_base.model.GPTChatRequest
import com.brins.lib_base.model.GPTChatResponse
import com.brins.lib_base.model.OpenAIReuqest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IChatGPTService {
  @Headers(
    "accept: text/event-stream",
    "accept-encoding: gzip, deflate, br",
    "accept-language: en-GB,en-US;q=0.9,en;q=0.8",
    "content-type: application/json",
  )
  @POST("backend-api/conversation")
  suspend fun sendMessage(@Body body: GPTChatRequest): Response<GPTChatResponse>


  @POST("v1/chat/completions")
  suspend fun createCompletion(@Body body: GPTChatRequest): Response<GPTChatResponse>

}
