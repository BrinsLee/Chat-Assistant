package com.brins.gpt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brins.gpt.repository.IGPTMessageRepository
import com.brins.lib_base.config.MODEL_3_5_TURBO
import com.brins.lib_base.config.chatGPTUser
import com.brins.lib_base.extensions.toGPTMessage
import com.brins.lib_base.extensions.toMessage
import com.brins.lib_base.model.GPTChatRequest
import com.brins.lib_network.error.ErrorCode
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Message
import io.getstream.chat.android.models.User
import io.getstream.result.Result
import io.getstream.result.onSuccessSuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatGPTMessageViewModel @Inject constructor(
    private val gptMessageRepository: IGPTMessageRepository
) : ViewModel() {

    private val _isMessageEmpty: MutableLiveData<Boolean> = MutableLiveData()
    val isMessageEmpty: LiveData<Boolean> = _isMessageEmpty

    private val _typingState: MutableLiveData<TypingState> = MutableLiveData(TypingState.Normal)
    val typingState: LiveData<TypingState> = _typingState

    private val _errorEvents: MutableLiveData<ErrorEvent> = MutableLiveData()
    val errorEvents: LiveData<ErrorEvent> = _errorEvents

    fun createCompletion(message: Message, model: String) {
        _typingState.value = TypingState.Typing(chatGPTUser)
        viewModelScope.launch {
            val gptRequest: GPTChatRequest =
                GPTChatRequest(messages = listOf(message.toGPTMessage()), model)
            val result = gptMessageRepository.createCompletion(gptRequest)
            if (result != null) {
                gptMessageRepository.sendStreamMessage(result.toMessage(message.cid)).await()
            } else {
                _errorEvents.postValue(ErrorEvent.SendMessageError(ErrorCode.NETWORK_FAILED))
            }
            _typingState.value = TypingState.Normal
        }
    }

    fun sendStreamChatMessage(cid: String, text: String, isFromMine: Boolean) {
        viewModelScope.launch {
            val result = gptMessageRepository.sendStreamMessage(cid, text, isFromMine).await()
            // todo 异常处理
            when (result) {
                is Result.Success -> {}
                is Result.Failure -> {}
            }
        }
    }

    fun sendStreamChatMessage(message: Message, channel: Channel, isFromMine: Boolean) {
        viewModelScope.launch {
//            val result = gptMessageRepository.sendStreamMessage(message, isFromMine).await()
            /*when (result) {
                is Result.Success -> {
                    if (channel.extraData.containsKey("model")) {
                        createCompletion(result.value, channel.extraData["model"] as String)
                    } else {
                        createCompletion(result.value, MODEL_3_5_TURBO)
                    }
                }
                is Result.Failure -> {
                    _errorEvents.postValue(ErrorEvent.SendMessageError(ErrorCode.NETWORK_FAILED))
                }
            }*/
            if (channel.extraData.containsKey("model")) {
                createCompletion(message, channel.extraData["model"] as String)
            } else {
                createCompletion(message, MODEL_3_5_TURBO)
            }
        }
    }


    fun checkIsEmptyMessage(cid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = gptMessageRepository.watchIsChannelMessageEmpty(cid).await()
                result.onSuccessSuspend {
                   _isMessageEmpty.postValue(it.messages.isEmpty())
                }
            }

        }
    }


    /**
     * ====================================== TypingState ===============================================
     */
    sealed class TypingState {
        class Typing(val user: User): TypingState()

        object Normal: TypingState()
    }

    /**
     * ====================================== SendMessageErrorEvent ===============================================
     */
    sealed class ErrorEvent(open val error: ErrorCode) {
        data class SendMessageError(override val error: ErrorCode) : ErrorEvent(error)

//        data class CreateChannelError(override val streamError: Error) : ErrorEvent(streamError)
    }



}