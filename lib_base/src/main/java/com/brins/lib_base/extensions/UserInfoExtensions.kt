package com.brins.lib_base.extensions

import com.brins.lib_base.config.chatGPTUser
import com.google.gson.Gson
import io.getstream.chat.android.models.User

fun User.toJson(): String {
    return Gson().toJson(this)
}

fun User.isChatGPT(): Boolean {
    return this.id == chatGPTUser.id
}

inline fun<reified T> fromJson(data: String): T {
    return Gson().fromJson(data, T::class.java)
}