package com.brins.lib_base.extensions

import android.annotation.SuppressLint
import com.brins.lib_base.config.ChatModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel
import io.getstream.result.call.Call

fun Channel.isSameChannel(channel: Channel?): Boolean {
    if (channel == null) {
        return false
    }
    return (this.getComparableField("cid") == channel.getComparableField("cid") &&
            this.getComparableField("id") == channel.getComparableField("id"))
}

fun Channel.isChatGPTChannel(): Boolean {
    return this.extraData.containsKey("model") && (this.extraData["model"] as String).contains("gpt")
}

fun Channel.isDallChannel(): Boolean {
    return this.extraData.containsKey("model") && (this.extraData["model"] as String).contains("dall")
}

fun Channel.getChannelSimpleName(): String {
    return when(this.extraData["model"].toString()) {
        ChatModel.ChatGPT_3_5_TURBO.modelName, ChatModel.ChatGPT_3_5_TURBO_1106.modelName -> {
            ChatModel.ChatGPT_3_5_TURBO.simpleName
        }
        ChatModel.ChatGPT_4_VISION_PREVIEW.modelName, ChatModel.ChatGPT_4_1106_PREVIEW.modelName -> {
            ChatModel.ChatGPT_4_1106_PREVIEW.simpleName
        }
        ChatModel.DALL_E_2.modelName, ChatModel.DALL_E_3.modelName -> {
            ChatModel.DALL_E_3.simpleName
        }
        else -> {
            ""
        }
    }
}

@SuppressLint("CheckResult")
fun Channel.updatePartial(map: Map<String, Any>) {
    ChatClient.instance().channel(cid).updatePartial(set = map).execute()
}

@SuppressLint("CheckResult")
fun Channel.updatePartial(map: Map<String, Any>, callback: Call.Callback<Channel>) {
    ChatClient.instance().channel(cid).updatePartial(set = map).enqueue(callback)
}