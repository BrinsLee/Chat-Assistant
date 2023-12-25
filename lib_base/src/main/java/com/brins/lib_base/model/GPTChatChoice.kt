package com.brins.lib_base.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class GPTChatChoice(
    @field:Json(name = "index") val index: Int,
    @field:Json(name = "message") val message: GPTMessage?,
    @field:Json(name = "finish_reason") val finishreason: String?) : Serializable {

}