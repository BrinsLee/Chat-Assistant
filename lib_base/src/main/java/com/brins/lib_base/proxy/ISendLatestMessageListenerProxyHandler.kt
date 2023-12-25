package com.brins.lib_base.proxy

import android.util.Log
import com.brins.lib_base.extensions.isChatGPT
import com.brins.lib_base.extensions.isFromChatGPT
import io.getstream.chat.android.client.plugin.listeners.SendMessageListener
import io.getstream.chat.android.models.Message
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import io.getstream.result.Result

class ISendLatestMessageListenerProxyHandler(private val originalListener: SendMessageListener): InvocationHandler {

    companion object {
        const val TAG = "ISendMessageListenerProxyHandler"
        const val METHOD_NAME = "onMessageSendResult"
    }

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {

        if (METHOD_NAME == method.name) {
            try {
                Log.d(TAG, "拦截方法：${METHOD_NAME}")
                val rawResult: Result<Message> = args!![0] as Result<Message>
                val message: Message = args!![3] as Message
                if (message.isFromChatGPT()) {
                    Log.d(TAG, "拦截方法：isFromChatGPT true")
                    val newResult: Result<Message>
                    when (rawResult) {
                        is Result.Success -> { newResult = Result.Success(message) }
                        is Result.Failure -> { newResult = Result.Failure(rawResult.value) }
                    }
                    return method.invoke(originalListener, newResult, args[1], args[2], args[3], args[4])
                } else {
                    Log.d(TAG, "拦截方法：isFromChatGPT false")
                    return method.invoke(originalListener, args)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return method.invoke(originalListener, args)

    }


}