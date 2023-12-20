package com.brins.gpt.repository

import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.User
import io.getstream.result.call.Call
import kotlinx.coroutines.flow.Flow

interface IGPTUserInfoRepository {


    fun streamUserAsFlow(): Flow<User?>
}