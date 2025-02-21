package com.brins.gpt.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brins.gpt.repository.IGPTMessageRepository
import com.brins.lib_base.config.ChatModel.Companion.MODEL_3_5_TURBO
import com.brins.lib_base.config.ChatModel.Companion.MODEL_4_VISION_PREVIEW
import com.brins.lib_base.config.chatGPTUser
import com.brins.lib_base.extensions.toGPTMessage
import com.brins.lib_base.extensions.toGPTMessageVision
import com.brins.lib_base.extensions.toMessage
import com.brins.lib_base.extensions.toStreamMessage
import com.brins.lib_base.model.GPTChatRequest
import com.brins.lib_base.model.vision.GPTChatRequestVision
import com.brins.lib_network.error.ErrorCode
import com.kunminx.architecture.domain.message.MutableResult
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Message
import io.getstream.chat.android.models.User
import io.getstream.chat.android.ui.common.state.messages.MessageMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatGPTMessageViewModel @Inject constructor(
    private val gptMessageRepository: IGPTMessageRepository
) : ViewModel() {

    private val _isMessageEmpty: MutableResult<Boolean> = MutableResult()
    val isMessageEmpty: com.kunminx.architecture.domain.message.Result<Boolean> = _isMessageEmpty

    private val _typingState: MutableLiveData<TypingState> = MutableLiveData(TypingState.Normal)
    val typingState: LiveData<TypingState> = _typingState

    private val _errorEvents: MutableResult<ErrorEvent> = MutableResult()
    val errorEvents: com.kunminx.architecture.domain.message.Result<ErrorEvent> = _errorEvents

    private val _streamMessage: MutableStateFlow<Message> = MutableStateFlow(Message())
    public val streamMessage: StateFlow<Message> = _streamMessage


    fun updateMessage(newMessage: Message) {
        _streamMessage.value = newMessage
    }
    private fun createCompletion(message: Message, model: String) {
        _typingState.value = TypingState.Typing(chatGPTUser)
        viewModelScope.launch {
            val gptChatRequest: GPTChatRequest =
                GPTChatRequest(messages = listOf(message.toGPTMessage()), model)
            val result = gptMessageRepository.createCompletion(gptChatRequest)
            if (result != null) {
                gptMessageRepository.sendStreamMessage(result.toMessage(message)).await()
            } else {
                _errorEvents.postValue(ErrorEvent.SendMessageError(ErrorCode.NETWORK_FAILED))
            }
            _typingState.value = TypingState.Normal
        }
    }

    private fun createCompletionStream(message: Message, model: String) {
        _typingState.value = TypingState.Typing(chatGPTUser)
        viewModelScope.launch {
            val stringBuffer = StringBuffer()
            var resultMessage: Message? = null
            val gptChatRequest: GPTChatRequest =
                GPTChatRequest(messages = listOf(message.toGPTMessage()), model, stream = true)
            gptMessageRepository.createCompletionStream(gptChatRequest, onStart = { firstChunk ->
                gptMessageRepository.sendStreamMessage(firstChunk.toStreamMessage(message)).await()
                    .onSuccess { msg ->
                        stringBuffer.append(firstChunk.choices[0].delta?.content ?: "")
                        resultMessage = msg
                        Log.d("lpl", "sendStreamMessage success")
                        updateMessage(msg)
                    }.onError {
                        // 发送失败直接取消
                        cancel()
                    }
            }, onChunkReceived = { response ->
                resultMessage?.let {
                    for (choice in response.choices) {
                        if (choice.finishreason != "stop") {
                            if (choice.delta?.content != null) {
                                stringBuffer.append(choice.delta!!.content)
                            }
                        }
                    }
                    Log.d("lpl", "onChunkReceived $stringBuffer")
                    updateMessage(it.copy(text = stringBuffer.toString()))
                }

            }, onComplete = {
                resultMessage?.let {
                    Log.d("lpl", "onComplete $stringBuffer")
                    gptMessageRepository.updateStreamMessage(it.copy(text = stringBuffer.toString()))
                }
                _typingState.postValue(TypingState.Normal)
            }, onError = {
                _errorEvents.postValue(ErrorEvent.SendMessageError(ErrorCode.NETWORK_FAILED))
                _typingState.postValue(TypingState.Normal)
            })
        }
    }

    /*private fun createCompletionStream(message: Message, model: String) {
        _typingState.value = TypingState.Typing(chatGPTUser)
        viewModelScope.launch {
            val stringBuffer = StringBuffer()
            var resultMessage: Message? = null
            val gptChatRequest: GPTChatRequest =
                GPTChatRequest(messages = listOf(message.toGPTMessage()), model, stream = true)
            gptMessageRepository.createCompletionStream(gptChatRequest, onStart = { firstChunk ->
                gptMessageRepository.sendStreamMessage(firstChunk.toStreamMessage(message)).await()
                    .onSuccess { msg ->
                        stringBuffer.append(firstChunk.choices[0].delta?.content ?: "")
                        resultMessage = msg
                        updateMessage(msg)
                    }.onError {
                    // 发送失败直接取消
                    cancel()
                }
            }, onChunkReceived = { response ->
                resultMessage?.let {
                    for (choice in response.choices) {
                        if (choice.finishreason != "stop") {
                            if (choice.delta?.content != null) {
                                stringBuffer.append(choice.delta!!.content)
                            }
                        }
                    }
                    updateMessage(it.copy(text = stringBuffer.toString()))
                }

            }, onComplete = {
                resultMessage?.let {
                    gptMessageRepository.updateStreamMessage(it.copy(text = stringBuffer.toString()))
                }
                _typingState.postValue(TypingState.Normal)
            }, onError = {
                _errorEvents.postValue(ErrorEvent.SendMessageError(ErrorCode.NETWORK_FAILED))
                _typingState.postValue(TypingState.Normal)
            })
        }
    }*/


    private fun createCompletionVision(message: Message, model: String = MODEL_4_VISION_PREVIEW) {
        _typingState.value = TypingState.Typing(chatGPTUser)
        viewModelScope.launch {
            val gptChatRequestVision: GPTChatRequestVision =
                GPTChatRequestVision(messages = listOf(message.toGPTMessageVision()), model)
            val result = gptMessageRepository.createCompletion(gptChatRequestVision)
            if (result != null) {
                gptMessageRepository.sendStreamMessage(result.toMessage(message)).await()
            } else {
                _errorEvents.postValue(ErrorEvent.SendMessageError(ErrorCode.NETWORK_FAILED))
            }
            _typingState.value = TypingState.Normal
        }

    }


    /*    fun sendStreamChatMessage(cid: String, text: String, isFromMine: Boolean) {
            viewModelScope.launch {
                val result = gptMessageRepository.sendStreamMessage(cid, text, isFromMine).await()
                // todo 异常处理
                when (result) {
                    is Result.Success -> {}
                    is Result.Failure -> {}
                }
            }
        }*/

    fun sendChatMessage(message: Message, channel: Channel, isFromMine: Boolean) {
        viewModelScope.launch {
            /**
             * 根据消息选择模型
             */
            if (message.attachments.isNotEmpty()) {
                //有附件，使用 MODEL_4_VISION_PREVIEW
                createCompletionVision(message)
            } else if (channel.extraData.containsKey("model")) {
                createCompletion(message, channel.extraData["model"] as String)
            } else {
                createCompletion(message, MODEL_3_5_TURBO)
            }
        }
    }

    fun sendStreamChatMessage(message: Message, channel: Channel, isFromMine: Boolean) {
        viewModelScope.launch {
            /**
             * 根据消息选择模型
             */
            if (message.attachments.isNotEmpty()) {
                //有附件，使用 MODEL_4_VISION_PREVIEW
                createCompletionVision(message)
            } else if (channel.extraData.containsKey("model")) {
                createCompletionStream(message, channel.extraData["model"] as String)
            } else {
                createCompletionStream(message, MODEL_3_5_TURBO)
            }
        }
    }


    fun checkIsEmptyMessage(cid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gptMessageRepository.watchIsChannelMessageEmpty(cid) { channel ->
                    if (channel != null) {
                        _isMessageEmpty.postValue(channel.messages.isEmpty())
                    } else {
                        //todo 错误处理
                    }
                }
            }

        }
    }


    /**
     * ====================================== TypingState ===============================================
     */
    sealed class TypingState {
        class Typing(val user: User) : TypingState()

        object Normal : TypingState()
    }

    /**
     * ====================================== SendMessageErrorEvent ===============================================
     */
    sealed class ErrorEvent(open val error: ErrorCode) {
        data class SendMessageError(override val error: ErrorCode) : ErrorEvent(error)

//        data class CreateChannelError(override val streamError: Error) : ErrorEvent(streamError)
    }


}