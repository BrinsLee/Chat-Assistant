package com.brins.lib_base.model.image

import com.brins.lib_base.config.ChatModel.Companion.MODEL_DALL_E_3
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

const val QUALITY_DETAIL_HD = "hd"
const val QUALITY_DETAIL_STANDARD = "standard"

const val IMAGE_SIZE_1024_1024 = "1024x1024"
const val IMAGE_SIZE_1792_1024 = "1792x1024"
const val IMAGE_SIZE_1024_1792 = "1024x1792"
@JsonClass(generateAdapter = true)
data class GPTImageRequest(
    @field:Json(name = "prompt")val prompt: String,
    @field:Json(name = "model")val model: String = MODEL_DALL_E_3,
    @field:Json(name = "n")val num: Int = 1,
    @field:Json(name = "quality")val quality: String = QUALITY_DETAIL_STANDARD,
    @field:Json(name = "size")val size: String = IMAGE_SIZE_1024_1024
)
