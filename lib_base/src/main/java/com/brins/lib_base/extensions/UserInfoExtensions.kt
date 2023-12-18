package com.brins.lib_base.extensions

import com.google.gson.Gson
import io.getstream.chat.android.models.User

fun User.toJson(): String {
    return Gson().toJson(this)
}

inline fun<reified T> fromJson(data: String): T {
    return Gson().fromJson(data, T::class.java)
}