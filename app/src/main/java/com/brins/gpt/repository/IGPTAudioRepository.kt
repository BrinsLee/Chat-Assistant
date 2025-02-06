package com.brins.gpt.repository

import com.brins.lib_base.model.audio.GPTTextToSpeechRequest
import okhttp3.ResponseBody

/**
 * @author lipeilin
 * @date 2024/4/4
 * @desc
 */
interface IGPTAudioRepository {

    /**
     * 文本转语音
     */
    suspend fun messageTextToSpeech(gptTextToSpeechRequest: GPTTextToSpeechRequest): ResponseBody?
}