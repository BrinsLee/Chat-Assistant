package com.brins.gpt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brins.gpt.repository.GPTImageRepositoryImpl
import com.brins.gpt.repository.IGPTImageRepository
import com.brins.lib_base.config.ChatModel.Companion.MODEL_3_5_TURBO
import com.brins.lib_base.config.ChatModel.Companion.MODEL_DALL_E_3
import com.brins.lib_base.config.chatGPTUser
import com.brins.lib_base.extensions.toMessage
import com.brins.lib_base.model.image.GPTImageRequest
import com.brins.lib_network.error.ErrorCode
import com.kunminx.architecture.domain.message.MutableResult
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
class ChatGPTImageViewModel @Inject constructor(private val gptImageRepository: IGPTImageRepository): ViewModel() {


    private val _isMessageEmpty: MutableResult<Boolean> = MutableResult()
    val isMessageEmpty: com.kunminx.architecture.domain.message.Result<Boolean> = _isMessageEmpty

    private val _typingState: MutableLiveData<TypingState> = MutableLiveData(
        TypingState.Normal)
    val typingState: LiveData<TypingState> = _typingState

    private val _errorEvents: MutableResult<ErrorEvent> = MutableResult()
    val errorEvents: com.kunminx.architecture.domain.message.Result<ErrorEvent> = _errorEvents

    fun checkIsEmptyMessage(cid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gptImageRepository.watchIsChannelMessageEmpty(cid) { channel ->
                    if (channel != null) {
                        _isMessageEmpty.postValue(channel.messages.isEmpty())
                    } else {
                        //todo 错误处理
                    }
                }
            }

        }
    }

    fun sendStreamChatMessage(message: Message, channel: Channel, isFromMine: Boolean) {
        viewModelScope.launch {
            createImage(message, MODEL_DALL_E_3)
        }
    }

    fun sendStreamChatMessage(cid: String, text: String, isFromMine: Boolean) {
        viewModelScope.launch {
            val result = gptImageRepository.sendStreamMessage(cid, text, isFromMine).await()
            // todo 异常处理
            when (result) {
                is Result.Success -> {}
                is Result.Failure -> {}
            }
        }
    }

    fun createImage(message: Message, model: String) {
        _typingState.value = TypingState.Typing(chatGPTUser)
        viewModelScope.launch {
            val gptImageRequest = GPTImageRequest(message.text, model = model)
            val result = gptImageRepository.createImage(gptImageRequest)

            if (result != null) {
                gptImageRepository.sendStreamMessage(result.toMessage(message)).await()
            } else {
                _errorEvents.postValue(ErrorEvent.SendMessageError(ErrorCode.NETWORK_FAILED))
            }
            _typingState.value = TypingState.Normal
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