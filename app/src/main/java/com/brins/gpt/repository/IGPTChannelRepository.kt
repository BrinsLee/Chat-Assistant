package com.brins.gpt.repository

import com.brins.lib_base.config.MODEL_3_5_TURBO_1106
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.FilterObject
import io.getstream.chat.android.state.event.handler.chat.factory.ChatEventHandlerFactory
import io.getstream.chat.android.state.plugin.state.querychannels.QueryChannelsState
import io.getstream.result.call.Call
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IGPTChannelRepository {

    suspend fun createRandomChannel(model: String = MODEL_3_5_TURBO_1106): Call<Channel>

    fun buildDefaultFilter(): Flow<FilterObject>
    fun queryChannelsAsState(
        queryChannelsRequest: QueryChannelsRequest,
        chatEventHandlerFactory: ChatEventHandlerFactory,
        viewModelScope: CoroutineScope
    ): StateFlow<QueryChannelsState?>
}