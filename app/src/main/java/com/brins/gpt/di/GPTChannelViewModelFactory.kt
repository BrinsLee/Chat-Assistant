package com.brins.gpt.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brins.gpt.repository.GPTChannelRepositoryImpl
import com.brins.gpt.viewmodel.ChatGPTChannelStateViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.FilterObject
import io.getstream.chat.android.models.querysort.QuerySorter
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListViewModel

class GPTChannelViewModelFactory @JvmOverloads constructor(
    private val filter: FilterObject? = null,
    private val sort: QuerySorter<Channel> = ChannelListViewModel.DEFAULT_SORT,
    private val limit: Int = ChatGPTChannelStateViewModel.DEFAULT_CHANNEL_LIMIT,
    private val messageLimit: Int = ChatGPTChannelStateViewModel.DEFAULT_MESSAGE_LIMIT,
    private val memberLimit: Int = ChatGPTChannelStateViewModel.DEFAULT_MEMBER_LIMIT,
): ViewModelProvider.Factory {


    private var gptChannelRepositoryImpl: GPTChannelRepositoryImpl =
        GPTChannelRepositoryImpl(ChatClient.instance())

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatGPTChannelStateViewModel(filter, sort, limit, messageLimit, memberLimit, gptChannelRepositoryImpl) as T
    }
}