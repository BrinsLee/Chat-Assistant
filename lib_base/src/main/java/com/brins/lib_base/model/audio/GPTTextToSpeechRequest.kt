package com.brins.lib_base.model.audio

import com.brins.lib_base.config.MODEL_DALL_E_3
import com.brins.lib_base.config.VOICE_ALLOY
import com.brins.lib_base.config.VOICE_MODEL_TTS_1
import com.brins.lib_base.model.image.IMAGE_SIZE_1024_1024
import com.brins.lib_base.model.image.QUALITY_DETAIL_STANDARD
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GPTTextToSpeechRequest(
    @field:Json(name = "model")val model: String = VOICE_MODEL_TTS_1,
    @field:Json(name = "input")val input: String,
    @field:Json(name = "voice")val voice: String = VOICE_ALLOY,
    @field:Json(name = "response_format")val response_format: String = "mp3",
    @field:Json(name = "speed")val speed: Float = 1.0f
)