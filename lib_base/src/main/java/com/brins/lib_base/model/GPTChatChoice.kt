package com.brins.lib_base.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@JsonClass(generateAdapter = true)
@Parcelize
data class GPTChatChoice(
    @field:Json(name = "delta") val delta: Delta? = null,
    @field:Json(name = "index") val index: Int,
    @field:Json(name = "message") val message: GPTMessage? = null,
    @field:Json(name = "finish_reason") val finishreason: String?) : Parcelable, Serializable


@JsonClass(generateAdapter = true)
@Parcelize
data class Delta(var role: String? = null,
    var content: String? = null): Parcelable, Serializable