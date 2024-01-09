package com.brins.gpt.repository

import com.brins.gpt.ChatApp
import com.brins.lib_base.config.CHANNEL_NAME_PREFIX
import com.brins.lib_base.config.MODEL_3_5_TURBO
import com.brins.lib_base.config.MODEL_3_5_TURBO_1106
import com.brins.lib_base.config.MODEL_4_1106_PREVIEW
import com.brins.lib_base.config.MODEL_4_VISION_PREVIEW
import com.brins.lib_base.config.chatGPTUser
import com.brins.lib_base.extensions.defaultChannelListFilter
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.FilterObject
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.state.event.handler.chat.factory.ChatEventHandlerFactory
import io.getstream.chat.android.state.extensions.queryChannelsAsState
import io.getstream.chat.android.state.plugin.state.querychannels.QueryChannelsState
import io.getstream.chat.android.ui.common.helper.DateFormatter
import io.getstream.result.call.Call
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.util.Date
import java.util.UUID
import javax.inject.Inject


class GPTChannelRepositoryImpl @Inject constructor(private val chatClient: ChatClient) :
    IGPTChannelRepository {

    override suspend fun createRandomChannel(model: String): Call<Channel> {
        val userId = chatClient.getCurrentUser()?.id ?: ""
        return chatClient.createChannel(
            channelType = "messaging",
            channelId = UUID.randomUUID().toString(),
            memberIds = listOf(userId, chatGPTUser.id),
            extraData = mapOf("name" to generateChannelName(model), "model" to model)
        )
    }

    override fun buildDefaultFilter(): Flow<FilterObject> {
        return chatClient.clientState.user.map(Filters::defaultChannelListFilter).filterNotNull()
    }


    /**
     * 使用state 必须开启StatePlugin 插件 [@see][ChatApp.initStreamChat]
     */
    override fun queryChannelsAsState(
        queryChannelsRequest: QueryChannelsRequest,
        chatEventHandlerFactory: ChatEventHandlerFactory,
        viewModelScope: CoroutineScope
    ): StateFlow<QueryChannelsState?> {
        return chatClient.queryChannelsAsState(queryChannelsRequest, chatEventHandlerFactory, viewModelScope)
    }

    override fun queryChannel(queryChannelsRequest: QueryChannelsRequest): Call<List<Channel>> {
        return chatClient.queryChannels(queryChannelsRequest)
    }

    private fun generateChannelName(model: String): String {
        val builder: StringBuilder = StringBuilder()
        builder.append(CHANNEL_NAME_PREFIX)
        when(model) {
            MODEL_3_5_TURBO_1106, MODEL_3_5_TURBO -> builder.append(3)
            MODEL_4_1106_PREVIEW, MODEL_4_VISION_PREVIEW -> builder.append(4)
        }
        val currentDate = Date()
        val date = ChatApp.dateDefaultFormat.formatDateTime(currentDate)
        builder.append(" ")
        builder.append(date)
        return builder.toString()
    }

    override fun deleteChannel(channel: Channel): Call<Channel> {
        return chatClient.channel(channel.cid).delete()
    }
}