package com.brins.gpt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brins.gpt.repository.IGPTMessageRepository
import com.brins.lib_base.model.GPTChatRequest
import com.brins.lib_base.model.GPTMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatGPTMessageViewModel @Inject constructor(
    private val gptMessageRepository: IGPTMessageRepository):ViewModel() {


        fun createCompletion() {
            viewModelScope.launch {
                val gptRequest: GPTChatRequest = GPTChatRequest("", messages = listOf(GPTMessage("")), parent_message_id = "")
                gptMessageRepository.createCompletion(gptRequest)
            }
        }
}