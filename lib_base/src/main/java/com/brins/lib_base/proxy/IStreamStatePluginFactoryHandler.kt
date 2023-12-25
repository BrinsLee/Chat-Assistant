package com.brins.lib_base.proxy

import android.util.Log
import com.brins.lib_base.utils.HookHelper
import com.brins.lib_base.utils.ReflectionUtils
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.plugin.factory.PluginFactory
import io.getstream.chat.android.client.plugin.listeners.SendMessageListener
import io.getstream.chat.android.models.User
import io.getstream.log.StreamLog
import kotlinx.coroutines.CoroutineExceptionHandler
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class IStreamStatePluginFactoryHandler(private val originalFactory: PluginFactory): InvocationHandler {

        companion object {
            const val TAG = "IStreamStatePluginFactoryHandler"
            const val METHOD_NAME = "get"

            const val QueryChannelListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.QueryChannelListenerDatabase"
            const val ThreadQueryListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.ThreadQueryListenerDatabase"
            const val EditMessageListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.EditMessageListenerDatabase"
            const val HideChannelListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.HideChannelListenerDatabase"
            const val DeleteReactionListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.DeleteReactionListenerDatabase"
            const val SendReactionListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.SendReactionListenerDatabase"
            const val DeleteMessageListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.DeleteMessageListenerDatabase"
            const val SendMessageListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.SendMessageListenerDatabase"
            const val SendAttachmentsListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.SendAttachmentsListenerDatabase"
            const val ShuffleGiphyListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.ShuffleGiphyListenerDatabase"
            const val QueryMembersListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.QueryMembersListenerDatabase"
            const val CreateChannelListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.CreateChannelListenerDatabase"
            const val GetMessageListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.GetMessageListenerDatabase"
            const val FetchCurrentUserListenerDatabase_NAME = "io.getstream.chat.android.offline.plugin.listener.internal.FetchCurrentUserListenerDatabase"

            const val OfflinePlugin_NAME = "io.getstream.chat.android.offline.plugin.internal.OfflinePlugin"

        }

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {

        if (METHOD_NAME == method.name) {
            Log.d(TAG, "拦截方法：$METHOD_NAME")
            return createStatePlugin(if (args == null) ChatClient.instance().getCurrentUser() else args[0] as? User)
        }
        return method.invoke(originalFactory, args)
    }

    private fun createStatePlugin(user: User?): Any {
        return try {
            val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
                StreamLog.e("StreamStatePlugin", throwable) {
                    "[uncaughtCoroutineException] throwable: $throwable, context: $context"
                }
            }




            setChatClientOfflineEnable(true)
            /*val clazz = Class.forName("io.getstream.chat.android.offline.plugin.internal.OfflinePlugin")
            val constructor = clazz.constructors[0]
            constructor.isAccessible = true
            constructor.newInstance()*/
            val chatClient = ChatClient.instance()
            val clientState = chatClient.clientState
            val repositoryFacade = ReflectionUtils.getProperty<ChatClient, Any>(chatClient, "repositoryFacade")
            val queryChannelListener = ReflectionUtils.createInstance(
                QueryChannelListenerDatabase_NAME, repositoryFacade)

            val threadQueryListener = ReflectionUtils.createInstance(
                ThreadQueryListenerDatabase_NAME, repositoryFacade, repositoryFacade)

            val editMessageListener = ReflectionUtils.createInstance(
                EditMessageListenerDatabase_NAME, repositoryFacade, repositoryFacade, clientState)

            val hideChannelListener = ReflectionUtils.createInstance(
                HideChannelListenerDatabase_NAME, repositoryFacade, repositoryFacade)

            val deleteReactionListener = ReflectionUtils.createInstance(
                DeleteReactionListenerDatabase_NAME, clientState, repositoryFacade, repositoryFacade)

            val sendReactionListener = ReflectionUtils.createInstance(
                SendReactionListenerDatabase_NAME, clientState, repositoryFacade, repositoryFacade, repositoryFacade)

            val deleteMessageListener = ReflectionUtils.createInstance(
                DeleteMessageListenerDatabase_NAME, clientState, repositoryFacade, repositoryFacade)

            //替换
            val sendMessageListener = ReflectionUtils.createInstance(
                SendMessageListenerDatabase_NAME, repositoryFacade, repositoryFacade)

            val sendMessageListenerProxy = HookHelper.hookSendMessageListener(sendMessageListener as SendMessageListener)

            val sendAttachmentListener = ReflectionUtils.createInstance(
                SendAttachmentsListenerDatabase_NAME, repositoryFacade, repositoryFacade)

            val shuffleGiphyListener = ReflectionUtils.createInstance(
                ShuffleGiphyListenerDatabase_NAME, repositoryFacade, repositoryFacade)

            val queryMembersListener = ReflectionUtils.createInstance(
                QueryMembersListenerDatabase_NAME, repositoryFacade, repositoryFacade)

            val createChannelListener = ReflectionUtils.createInstance(
                CreateChannelListenerDatabase_NAME, clientState, repositoryFacade, repositoryFacade)

            val getMessageListener = ReflectionUtils.createInstance(GetMessageListenerDatabase_NAME, repositoryFacade)

            val fetchCurrentUserListener = ReflectionUtils.createInstance(
                FetchCurrentUserListenerDatabase_NAME, repositoryFacade)

            val provideDependency: (kotlin.reflect.KClass<*>) -> kotlin.Any? = { klass -> "" as Any? }


            ReflectionUtils.createInstance(OfflinePlugin_NAME,
                user,
                queryChannelListener,
                threadQueryListener,
                editMessageListener,
                hideChannelListener,
                deleteReactionListener,
                sendReactionListener,
                deleteMessageListener,
                shuffleGiphyListener,
                sendMessageListenerProxy,
                sendAttachmentListener,
                queryMembersListener,
                createChannelListener,
                getMessageListener,
                fetchCurrentUserListener,
                provideDependency
                )!!

        } catch (e: Exception) {
            e.printStackTrace()
            originalFactory.get(user!!)
        }
    }

    private fun setChatClientOfflineEnable(boolean: Boolean) {
        ReflectionUtils.setStaticProperty(ChatClient.instance(), "OFFLINE_SUPPORT_ENABLED", boolean)
    }

}