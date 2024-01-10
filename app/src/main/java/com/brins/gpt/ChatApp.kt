package com.brins.gpt

import android.app.Application
import com.brins.lib_base.BuildConfig
import com.brins.lib_base.config.EXTRA_KEY_USER_DATA
import com.brins.lib_base.extensions.createUserAvatar
import com.brins.lib_base.extensions.createUserName
import com.brins.lib_base.extensions.fromJson
import com.brins.lib_base.extensions.toJson
import com.brins.lib_base.utils.AppUtils
import com.brins.lib_base.utils.formatter.ChatDateFormatter
import com.brins.lib_base.utils.CustomChatClientDebugger
import com.brins.lib_base.utils.MMKVUtils
import com.brins.lib_base.utils.formatter.ChatMessagePreviewFormatter
import dagger.hilt.android.HiltAndroidApp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.markdown.MarkdownTextTransformer
import io.getstream.chat.android.models.ConnectionData
import io.getstream.chat.android.models.UploadAttachmentsNetworkType
import io.getstream.chat.android.models.User
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import io.getstream.chat.android.ui.ChatUI
import io.getstream.log.streamLog
import io.getstream.result.call.Call
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

/**
 * 触发HiltApp
 * 生成一个Hilt_App，提供应用级别依赖
 * 是应用父组件，其他组件可以访问到他的依赖项
 */
@HiltAndroidApp
class ChatApp : Application() {

    @Inject
    lateinit var customChatClientDebugger: CustomChatClientDebugger

    companion object {
        @JvmStatic
        lateinit var dateDefaultFormat: ChatDateFormatter
            private set
        @JvmStatic
        lateinit var messagePreviewFormatter: ChatMessagePreviewFormatter
            private set
    }

    override fun onCreate() {
        super.onCreate()
        AppUtils.init(this)
        initFormatter()
        initMMKV()
        initStreamChat()
    }

    private fun initFormatter() {
        dateDefaultFormat = ChatDateFormatter()
        messagePreviewFormatter = ChatMessagePreviewFormatter()
        ChatUI.dateFormatter = dateDefaultFormat
        ChatUI.messagePreviewFormatter = messagePreviewFormatter
    }

    private fun initMMKV() {
        MMKVUtils.init(this)
    }

    private fun initStreamChat() {
        val logLevel = if (BuildConfig.DEBUG) ChatLogLevel.ALL else ChatLogLevel.NOTHING
        val offlinePluginFactory = StreamOfflinePluginFactory(
            appContext = this
        )
//        val proxyOfflinePluginFactory = HookHelper.hookStreamOfflinePluginFactory(offlinePluginFactory)
        val statePluginFactory = StreamStatePluginFactory(
            config = StatePluginConfig(
                backgroundSyncEnabled = true,
                userPresence = true,
            ),
            appContext = this,
        )
//        val proxyStatePluginFactory = HookHelper.hookStreamStatePluginFactory(statePluginFactory)
        val chatClient = ChatClient.Builder(BuildConfig.CHAT_API_KEY, this).logLevel(logLevel)
            .withPlugins(offlinePluginFactory, statePluginFactory)
            .uploadAttachmentsNetworkType(UploadAttachmentsNetworkType.NOT_ROAMING).apply {
                this.debugRequests(true).clientDebugger(customChatClientDebugger)
            }.build()
        createNewUser(chatClient)
        ChatUI.messageTextTransformer = MarkdownTextTransformer(this)

    }

    /**
     * 测试用
     */
    private fun createNewUser(chatClient: ChatClient) {
        if (BuildConfig.DEBUG) {
            val user: User = User(
                id = "5b87bb36-4303-43cc-9800-ed5777a20c98",
                name = "User 29",
                image = "https://www.picsum.photos/id/488/300/300")
            val token = chatClient.devToken(user.id)
            chatClient.connectUser(user, token).enqueue(object : Call.Callback<ConnectionData> {

                override fun onResult(result: io.getstream.result.Result<ConnectionData>) {
                    if (result.isFailure) {
                        streamLog {
                            "Can't connect user. Please check the app README.md and ensure " + "**Disable Auth Checks** is ON in the Dashboard"
                        }
                    } else if (result.isSuccess) {
                        MMKVUtils.putString(EXTRA_KEY_USER_DATA, user.toJson())
                    }
                }
            })
        } else {
            val userData = MMKVUtils.getString(EXTRA_KEY_USER_DATA, "")
            val user: User
            if (userData.isEmpty()) {
                val userId = UUID.randomUUID().toString()
                user = User(
                    id = userId,
                    name = "${Random.nextInt(10000)}".createUserName(),
                    image = "${Random.nextInt(1000)}".createUserAvatar()
                )
            } else {
                user = fromJson<User>(userData)
            }
            val token = chatClient.devToken(user.id)
            chatClient.connectUser(user, token).enqueue(object : Call.Callback<ConnectionData> {

                override fun onResult(result: io.getstream.result.Result<ConnectionData>) {
                    if (result.isFailure) {
                        streamLog {
                            "Can't connect user. Please check the app README.md and ensure " + "**Disable Auth Checks** is ON in the Dashboard"
                        }
                    } else if (result.isSuccess) {
                        MMKVUtils.putString(EXTRA_KEY_USER_DATA, user.toJson())
                    }
                }
            })
        }

    }
}