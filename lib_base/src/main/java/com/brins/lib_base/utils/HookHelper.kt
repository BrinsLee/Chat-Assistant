package com.brins.lib_base.utils

import com.brins.lib_base.proxy.ISendMessageListenerProxyHandler
import com.brins.lib_base.proxy.IStreamOfflinePluginFactoryHandler
import com.brins.lib_base.proxy.IStreamStatePluginFactoryHandler
import io.getstream.chat.android.client.plugin.factory.PluginFactory
import io.getstream.chat.android.client.plugin.listeners.SendMessageListener
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import java.lang.reflect.Proxy

object HookHelper {

    fun hookStreamOfflinePluginFactory(offlinePluginFactory: StreamOfflinePluginFactory): PluginFactory {
        try {
            return Proxy.newProxyInstance(
                offlinePluginFactory.javaClass.classLoader,
                arrayOf(PluginFactory::class.java),
                IStreamOfflinePluginFactoryHandler(offlinePluginFactory)
            ) as PluginFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return offlinePluginFactory
    }

    fun hookStreamStatePluginFactory(statePluginFactory: StreamStatePluginFactory): PluginFactory {
        try {
            return Proxy.newProxyInstance(
                statePluginFactory.javaClass.classLoader,
                arrayOf(PluginFactory::class.java),
                IStreamStatePluginFactoryHandler(statePluginFactory)
            ) as PluginFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statePluginFactory
    }

    fun hookSendMessageListener(originalListener: SendMessageListener): SendMessageListener {
        try {
            return Proxy.newProxyInstance(originalListener.javaClass.classLoader,
                arrayOf(SendMessageListener::class.java),
                ISendMessageListenerProxyHandler(originalListener)
            ) as SendMessageListener
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return originalListener
    }
}