package com.brins.lib_base.extensions

import android.content.Context
import com.brins.lib_base.config.chatGPTUser
import com.google.gson.Gson
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User
import io.getstream.chat.android.ui.ChatUI
import io.getstream.chat.android.ui.R

fun User.toJson(): String {
    return Gson().toJson(this)
}

fun User.isChatGPT(): Boolean {
    return this.id == chatGPTUser.id
}

fun User.isCurrentUser(): Boolean {
    return id == ChatClient.instance().getCurrentUser()?.id
}

fun User.asMention(context: Context): String =
    context.getString(R.string.stream_ui_mention, name)

inline fun<reified T> fromJson(data: String): T {
    return Gson().fromJson(data, T::class.java)
}