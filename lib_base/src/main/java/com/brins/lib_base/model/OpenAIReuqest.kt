package com.brins.lib_base.model

import com.brins.lib_base.config.ChatModel.Companion.MODEL_3_5_TURBO
import java.io.Serializable

data class OpenAIReuqest(var model: String = MODEL_3_5_TURBO, val messages: List<Message>, val temperature: Float): Serializable {}


data class Message(val role: String, val content: String): Serializable