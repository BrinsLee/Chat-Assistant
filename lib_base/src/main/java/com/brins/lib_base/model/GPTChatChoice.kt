package com.brins.lib_base.model

import java.io.Serializable

data class GPTChatChoice(val text: String,
    val index: Int,
    val logprobs: Any?,
    val finish_reason: String) : Serializable {

}