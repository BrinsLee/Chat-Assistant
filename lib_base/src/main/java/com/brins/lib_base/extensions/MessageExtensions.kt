package com.brins.lib_base.extensions

import io.getstream.chat.android.models.Message

fun Message.isSameMessage(message: Message): Boolean {
    return this.identifierHash() == message.identifierHash()
}