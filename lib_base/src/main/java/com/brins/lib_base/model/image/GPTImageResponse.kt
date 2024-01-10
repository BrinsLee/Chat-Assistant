package com.brins.lib_base.model.image

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GPTImageResponse(
    @field:Json(name = "created") val created: Long,
    @field:Json(name = "data") val data: List<GPTImageData>
)

@JsonClass(generateAdapter = true)
data class GPTImageData(
    @field:Json(name = "revised_prompt") val revisedPrompt: String = "",
    @field:Json(name = "url") val url: String
)
