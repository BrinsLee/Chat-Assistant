package com.brins.lib_base.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

/**
 * ChatGPT 非流式响应
 */
@JsonClass(generateAdapter = true)
data class GPTChatResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "object") val `object`: String,
    @field:Json(name = "model") val model: String,
    @field:Json(name = "created") val created: Long = 0,
    @field:Json(name = "system_fingerprint") val systemFingerprint: String?,
    @field:Json(name = "choices") val choices: List<GPTChatChoice>,
    @field:Json(name = "usage") val usage: GPTUsage? = null
) : Serializable

fun emptyGPTChatResponse(): GPTChatResponse {
    return GPTChatResponse(
        "",
        "",
        "",
        0,
        "",
        emptyList(),
        GPTUsage(0, 0, 0)
    )
}
/**
 * DeepSeek 非流式响应
 */
/*@JsonClass(generateAdapter = true)
data class DeepSeekChatResponse(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "choices") val choices: List<GPTChatChoice>,
    @field:Json(name = "created") val created: Long,
    @field:Json(name = "model") val model: String,
    @field:Json(name = "system_fingerprint") val systemFingerprint: String,
    @field:Json(name = "object") val `object`: String,
    @field:Json(name = "usage") val usage: GPTUsage

): Serializable*/
