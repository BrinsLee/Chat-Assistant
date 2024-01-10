package com.brins.gpt.repository

import com.brins.lib_base.model.image.GPTImageRequest
import com.brins.lib_base.model.image.GPTImageResponse
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Message
import io.getstream.result.call.Call

interface IGPTImageRepository {

    /**
     * 创建图片
     */
    suspend fun createImage(gptImageRequest: GPTImageRequest): GPTImageResponse?

    /**
     * 判断当前channel消息是否为空
     */
    suspend fun watchIsChannelMessageEmpty(cid: String, callback: (channel: Channel?) -> Unit)

    /**
     * 发送stream消息
     */
    suspend fun sendStreamMessage(cid: String, text: String, isFromMine: Boolean): Call<Message>


    /**
     * 发送stream消息
     */
    suspend fun sendStreamMessage(message: Message): Call<Message>
}