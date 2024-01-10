package com.brins.lib_base.model

import com.brins.lib_base.config.MODEL_3_5_TURBO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class GPTChatRequest(
//  @field:Json(name = "conversation_id") val conversation_id: String?,
//  @field:Json(name = "action") val action: String = "next",
    @field:Json(name = "messages") val messages: List<GPTMessage>,
//  @field:Json(name = "parent_message_id") val parent_message_id: String,
    @field:Json(name = "model") val model: String = MODEL_3_5_TURBO,
    @field:Json(name = "max_tokens") val maxToken: Int = 300
)
