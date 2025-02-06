package com.brins.gpt.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brins.gpt.repository.IGPTAudioRepository
import com.brins.gpt.viewmodel.ChatGPTAudioViewModel.TextToSpeechState.Fail
import com.brins.gpt.viewmodel.ChatGPTAudioViewModel.TextToSpeechState.Nothing
import com.brins.gpt.viewmodel.ChatGPTAudioViewModel.TextToSpeechState.Success
import com.brins.lib_base.config.VOICE_ALLOY
import com.brins.lib_base.config.VOICE_MODEL_TTS_1
import com.brins.lib_base.model.audio.Audio
import com.brins.lib_base.model.audio.GPTTextToSpeechRequest
import com.brins.lib_base.utils.FileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.models.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * @author lipeilin
 * @date 2024/4/4
 * @desc
 */
@HiltViewModel
class ChatGPTAudioViewModel @Inject constructor(private val repository: IGPTAudioRepository) :
    ViewModel() {

    private val _textToSpeechState: MutableStateFlow<TextToSpeechState> = MutableStateFlow(Nothing)
    val textToSpeechState: StateFlow<TextToSpeechState> = _textToSpeechState

    fun textToSpeech(context: Context, message: Message, model: String = VOICE_MODEL_TTS_1, voice: String = VOICE_ALLOY) {
        val fileName = "tts_${message.id}.mp3"
        FileUtils.getCacheFile(context, fileName).onSuccess { file ->
            if (file.exists()) {
                _textToSpeechState.value = TextToSpeechState.Success(Audio(fileName, message, file.absolutePath, file,0))
            } else {
                val gptTextToSpeechRequest = GPTTextToSpeechRequest(model, message.text,voice)
                viewModelScope.launch {
                    val responseBody = repository.messageTextToSpeech(gptTextToSpeechRequest)
                    responseBody?.apply {
                        withContext(Dispatchers.IO) {
                            FileUtils.createFileInCacheDir(context, fileName).onSuccess { file ->
                                byteStream().use { inputStream ->
                                    FileOutputStream(file).use { outputStream ->
                                        val buffer = ByteArray(4096)
                                        var read: Int
                                        while (inputStream.read(buffer).also { read = it } != -1) {
                                            outputStream.write(buffer, 0, read)
                                        }
                                        outputStream.flush()
                                    }
                                }
                                _textToSpeechState.value = Success(Audio(fileName, message, file.absolutePath, file,0))
                            }.onError {
                                _textToSpeechState.value = Fail
                            }
                        }
                    }

                }
            }
        }.onError {
            _textToSpeechState.value = TextToSpeechState.Fail
        }

    }

    sealed class TextToSpeechState {
        class Success(val audio: Audio): TextToSpeechState()

        object Nothing: TextToSpeechState()

        object Fail: TextToSpeechState()
    }

}