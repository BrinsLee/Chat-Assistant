package com.brins.gpt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brins.gpt.repository.IGPTMessageRepository
import com.brins.lib_base.model.GPTChatRequest
import com.brins.lib_base.model.GPTMessage
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun createCompletion() {
        viewModelScope.launch {
            val gptRequest: GPTChatRequest =
                GPTChatRequest("", messages = listOf(GPTMessage("")), parent_message_id = "")
            gptMessageRepository.createCompletion(gptRequest)
        }
    }

    fun sendStreamChatMessage(cid: String, text: String) {
        viewModelScope.launch {
            val result = gptMessageRepository.sendStreamMessage(cid, text).await()
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


}