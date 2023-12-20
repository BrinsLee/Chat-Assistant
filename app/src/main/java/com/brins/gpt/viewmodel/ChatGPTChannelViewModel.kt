package com.brins.gpt.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.brins.gpt.extensions.addFlow
import com.brins.gpt.extensions.combineWith
import com.brins.gpt.repository.GPTChannelRepositoryImpl
import com.brins.lib_base.config.MODEL_3_5_TURBO_1106
import com.brins.lib_base.config.MODEL_4_1106_PREVIEW
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.FilterObject
import io.getstream.chat.android.models.querysort.QuerySortByField
import io.getstream.chat.android.models.querysort.QuerySorter
import io.getstream.chat.android.state.event.handler.chat.factory.ChatEventHandlerFactory
import io.getstream.chat.android.state.plugin.state.querychannels.ChannelsStateData
import io.getstream.chat.android.state.plugin.state.querychannels.QueryChannelsState
import io.getstream.chat.android.ui.feature.channels.list.ChannelListView
import io.getstream.chat.android.ui.feature.channels.list.adapter.ChannelListItem
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListViewModel
import io.getstream.result.Error
import io.getstream.result.onSuccessSuspend
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

class ChatGPTChannelViewModel(
    private var filter: FilterObject? = null,
    private var sort: QuerySorter<Channel> = ChannelListViewModel.DEFAULT_SORT,
    private var limit: Int = DEFAULT_CHANNEL_LIMIT,
    private val messageLimit: Int = DEFAULT_MESSAGE_LIMIT,
    private val memberLimit: Int = DEFAULT_MEMBER_LIMIT,
    private val gptChannelRepositoryImpl: GPTChannelRepositoryImpl
) : ViewModel() {

    private var queryJob: Job? = null

    private val stateMerger = MediatorLiveData<ChannelState>()
    val channelState: LiveData<ChannelState> = stateMerger.distinctUntilChanged()

    private val paginationStateMerger = MediatorLiveData<PaginationState>()
    val paginationState: LiveData<PaginationState> = paginationStateMerger.distinctUntilChanged()

    private val _errorEvents: MutableLiveData<ErrorEvent> = MutableLiveData()
    val errorEvents: LiveData<ErrorEvent> = _errorEvents

    private val channelsMutableUiState =
        MutableStateFlow<CreateChannelState>(CreateChannelState.Nothing)
    val channelUiState: StateFlow<CreateChannelState> = channelsMutableUiState

    private val filterLiveData: MutableLiveData<FilterObject?> = MutableLiveData(filter)

    private var queryChannelsState: StateFlow<QueryChannelsState?> = MutableStateFlow(null)

    init {
        if (filter == null) {
            viewModelScope.launch {
                filter = buildDefaultFilter().first()
                this@ChatGPTChannelViewModel.filterLiveData.value = filter
            }
        }

        stateMerger.addSource(filterLiveData) { filter ->
            if (filter != null) {
                initData(filter)
            }
        }

    }

    private fun initData(filterObject: FilterObject) {
        stateMerger.value = INITIAL_STATE
        val queryChannelsRequest = QueryChannelsRequest(
            filter = filterObject,
            querySort = sort,
            limit = limit,
            messageLimit = messageLimit,
            memberLimit = memberLimit,
        )

        queryChannelsState = gptChannelRepositoryImpl.queryChannelsAsState(
            queryChannelsRequest,
            ChatEventHandlerFactory(),
            viewModelScope
        )

        queryJob?.cancel()
        val queryJob = Job(viewModelScope.coroutineContext.job).also {
            this.queryJob = it
        }
        viewModelScope.launch(queryJob) {
            queryChannelsState.filterNotNull().collectLatest { queryChannelsState ->
                if (!isActive) {
                    return@collectLatest
                }
                stateMerger.addFlow(
                    queryJob,
                    queryChannelsState.channelsStateData
                ) { channelState ->
                    stateMerger.value = handleChannelStateNews(channelState)
                }

                paginationStateMerger.addFlow(
                    queryJob,
                    queryChannelsState.loadingMore
                ) { loadingMore ->
                    setPaginationState { copy(loadingMore = loadingMore) }
                }
                paginationStateMerger.addFlow(
                    queryJob,
                    queryChannelsState.endOfChannels
                ) { endOfChannels ->
                    setPaginationState { copy(endOfChannels = endOfChannels) }
                }
            }
        }
    }

    companion object {

        @JvmField
        val DEFAULT_SORT: QuerySorter<Channel> = QuerySortByField.descByName("last_updated")

        const val DEFAULT_CHANNEL_LIMIT = 30

        const val DEFAULT_MESSAGE_LIMIT = 1

        const val DEFAULT_MEMBER_LIMIT = 3

    }


    private fun buildDefaultFilter(): Flow<FilterObject> {
        return gptChannelRepositoryImpl.buildDefaultFilter()
    }

    private fun handleChannelStateNews(
        channelState: ChannelsStateData,
    ): ChannelState {
        return when (channelState) {
            is ChannelsStateData.NoQueryActive,
            is ChannelsStateData.Loading,
            -> ChannelState(isLoading = true, emptyList())

            is ChannelsStateData.OfflineNoResults -> ChannelState(
                isLoading = false,
                channels = emptyList(),
            )

            is ChannelsStateData.Result -> ChannelState(
                isLoading = false, channels = channelState.channels
            )
        }
    }

    fun bindView(view: ChannelListView, lifecycleOwner: LifecycleOwner) {
        channelState.combineWith(paginationState) { channelState, paginationState ->
            paginationState?.let {
                view.setPaginationEnabled(!it.endOfChannels && !it.loadingMore)
            }

            var list: List<ChannelListItem> = channelState?.channels?.map {
                ChannelListItem.ChannelItem(it, emptyList())
            }?: emptyList()
            if (paginationState?.loadingMore == true) {
                list = list + ChannelListItem.LoadingMoreItem
            }
            list to (channelState?.isLoading == true)
        }.distinctUntilChanged().observe(lifecycleOwner) { (list, isLoading) ->
            when {
                isLoading && list.isEmpty() -> view.showLoadingView()
                list.isNotEmpty() -> {
                    view.hideLoadingView()
                    view.setChannels(list)
                }

                else -> {
                    view.hideLoadingView()
                    view.setChannels(emptyList())
                }
            }
        }

        errorEvents.observe(
            lifecycleOwner,
        ) {
//            view.showError(it)
        }
    }

    fun handleEvents(gptChannelEvent: GPTChannelEvent) {
        createRandomChannel(gptChannelEvent.model)
    }

    private fun createRandomChannel(model: String = MODEL_3_5_TURBO_1106) {
        viewModelScope.launch {
            channelsMutableUiState.value = CreateChannelState.Loading
            val result = gptChannelRepositoryImpl.createRandomChannel(model).await()
            result.onSuccessSuspend {
                channelsMutableUiState.value = CreateChannelState.Success(it.id)
                delay(100L)
                channelsMutableUiState.value = CreateChannelState.Nothing
            }.onError {
                channelsMutableUiState.value = CreateChannelState.Error
            }
        }
    }

    private fun setPaginationState(reducer: PaginationState.() -> PaginationState) {
        paginationStateMerger.value = reducer(paginationStateMerger.value ?: PaginationState())
    }

    /**
     * ====================================== ChannelState ===============================================
     */

    private val INITIAL_STATE: ChannelState = ChannelState(isLoading = true, channels = emptyList())

    data class ChannelState(val isLoading: Boolean, val channels: List<Channel>)

    /**
     * ====================================== PaginationState ===============================================
     */
    data class PaginationState(
        val loadingMore: Boolean = false,
        val endOfChannels: Boolean = false,
    )

    /**
     * ====================================== ErrorEvent ===============================================
     */
    sealed class ErrorEvent(open val streamError: Error) {
        data class DeleteChannelError(override val streamError: Error) : ErrorEvent(streamError)

        data class CreateChannelError(override val streamError: Error) : ErrorEvent(streamError)
    }


    /**
     * ====================================== GPTChannelEvent ===============================================
     */
    sealed class GPTChannelEvent (open val model: String) {
        class CreateChannelEvent3_5(model: String = MODEL_3_5_TURBO_1106) : GPTChannelEvent(model)

        class CreateChannelEvent4(model: String = MODEL_4_1106_PREVIEW): GPTChannelEvent(model)
    }

    /**
     * ====================================== CreateChannelState ===============================================
     */

    sealed interface CreateChannelState {
        object Nothing: CreateChannelState

        object Loading: CreateChannelState

        data class Success(val channelId: String): CreateChannelState

        object Error: CreateChannelState
    }

}