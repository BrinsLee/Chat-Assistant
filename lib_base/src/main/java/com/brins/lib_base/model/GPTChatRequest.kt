package com.brins.lib_base.model

import android.os.Parcelable
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.brins.lib_base.config.ChatModel.Companion.MODEL_3_5_TURBO
import com.brins.lib_base.config.ChatModel.Companion.MODEL_DEEP_SEEK_V3
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
data class GPTChatRequest(
//  @field:Json(name = "conversation_id") val conversation_id: String?,
//  @field:Json(name = "action") val action: String = "next",
    @field:Json(name = "messages") val messages: List<GPTMessage>,
//  @field:Json(name = "parent_message_id") val parent_message_id: String,
    @field:Json(name = "model") val model: String = MODEL_3_5_TURBO,
    @field:Json(name = "max_tokens") val maxToken: Int = 4096,
    @field:Json(name = "stream") val stream: Boolean = false
): Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class DeepSeekChatRequest(
    @field:Json(name = "message") val message: GPTMessage,
    @field:Json(name = "model") val model: String = MODEL_DEEP_SEEK_V3,
    /**
     * 介于 -2.0 和 2.0 之间的数字。如果该值为正，那么新 token 会根据其在已有文本中的出现频率受到相应的惩罚，降低模型重复相同内容的可能性。
     */
    @field:Json(name = "frequency_penalty")
    @FloatRange(from = -2.0, to = 2.0)
    val frequencyPenalty: Float = 0f,

    @field:Json(name = "max_tokens") val maxToken: Int = 4096,

    /**
     * 介于 -2.0 和 2.0 之间的数字。如果该值为正，那么新 token 会根据其是否已在已有文本中出现受到相应的惩罚，从而增加模型谈论新主题的可能性。
     */
    @field:Json(name = "presence_penalty")
    @FloatRange(from = -2.0, to = 2.0)
    val presencePenalty: Float = 0f,

    /**
     * 采样温度，介于 0 和 2 之间。更高的值，如 0.8，会使输出更随机，而更低的值，如 0.2，会使其更加集中和确定。 我们通常建议可以更改这个值或者更改 top_p，但不建议同时对两者进行修改。
     */
    @field:Json(name = "temperature")
    @FloatRange(from = 0.0, to = 2.0)
    val temperature: Float = 1f,

    /**
     * 作为调节采样温度的替代方案，模型会考虑前 top_p 概率的 token 的结果。所以 0.1 就意味着只有包括在最高 10% 概率中的 token 会被考虑。 我们通常建议修改这个值或者更改 temperature，但不建议同时对两者进行修改。
     */
    @field:Json(name = "top_p")
    @FloatRange(from = 0.0, to = 1.0)
    val topP: Float = 1f,

    @field:Json(name = "stream") val stream: Boolean = false
): Parcelable