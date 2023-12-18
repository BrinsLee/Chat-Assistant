package com.brins.lib_base.model

import java.io.Serializable


data class GPTChatResponse(
    val id: String,
    val `object`: String,
    val created: Int,
    val model: String,
) : Serializable {


}

