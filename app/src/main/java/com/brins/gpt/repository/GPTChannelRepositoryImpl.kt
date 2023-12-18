package com.brins.gpt.repository

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.User
import io.getstream.result.call.Call
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class GPTChannelRepositoryImpl @Inject constructor(
    private val chatClient: ChatClient
) : IGPTChannelRepository {
    override suspend fun createRandomChannel(): Call<Channel> {
        val userId = chatClient.getCurrentUser()?.id ?: ""
        return chatClient.createChannel(
            channelType = "messaging",
            channelId = UUID.randomUUID().toString(),
            memberIds = listOf(userId),
            extraData = mapOf("name" to "ChatGPT ${(0..99999).random()}")
        )
    }

    override fun streamUserAsFlow(): Flow<User?> {
        return chatClient.clientState.user
    }
}