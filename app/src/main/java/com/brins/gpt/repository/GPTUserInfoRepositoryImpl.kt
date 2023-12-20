package com.brins.gpt.repository

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.User
import io.getstream.result.call.Call
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class GPTUserInfoRepositoryImpl @Inject constructor(
    private val chatClient: ChatClient
) : IGPTUserInfoRepository {

    override fun streamUserAsFlow(): Flow<User?> {
        return chatClient.clientState.user
    }
}