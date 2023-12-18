package com.brins.lib_base.model

import com.brins.lib_base.config.MODEL_3_5
import java.io.Serializable

data class OpenAIReuqest(var model: String = MODEL_3_5, val messages: List<Message>, val temperature: Float): Serializable {}


data class Message(val role: String, val content: String): Serializable