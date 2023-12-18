package com.brins.gpt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brins.gpt.repository.GPTChannelRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.models.User
import io.getstream.log.streamLog
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatGPTChannelViewModel @Inject constructor(
    private val gptChannelRepositoryImpl: GPTChannelRepositoryImpl
) : ViewModel() {


    private val _userState = MutableLiveData<State>()
    val userState: LiveData<State> = _userState

    private val _events = MutableLiveData<UiEvent>()
    val events: LiveData<UiEvent> = _events

    fun fetchUserInfo() {
        _events.value = UiEvent.ShowLoading
        viewModelScope.launch {
            gptChannelRepositoryImpl.streamUserAsFlow().collect { user ->
                user?.let {
                    streamLog {
                        "User is $user"
                    }
                    _userState.value = State.AvailableUser(arrayListOf(user))
                } ?: run {
                    streamLog {
                        "User is null. Please check the app README.md and ensure " +
                                "**Disable Auth Checks** is ON in the Dashboard"
                    }
                }
                _events.value = UiEvent.HideLoading
            }
        }
    }

    sealed class State {
        data class AvailableUser(val availableUsers: List<User>): State()
    }

    sealed class UiEvent {
        data class UserClicked(val user: User): UiEvent()

        object ShowLoading: UiEvent()

        object HideLoading: UiEvent()

    }
}