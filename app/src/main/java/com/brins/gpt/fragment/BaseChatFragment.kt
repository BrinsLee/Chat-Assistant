package com.brins.gpt.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.brins.gpt.R
import com.brins.gpt.databinding.FragmentChatMessageBinding
import com.brins.gpt.viewmodel.ChatGPTAudioViewModel
import com.brins.gpt.viewmodel.ChatGPTMessageViewModel
import com.brins.lib_base.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.ui.common.state.messages.list.MessagePlayData
import io.getstream.chat.android.ui.feature.messages.list.MessageListView
import io.getstream.chat.android.ui.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.ui.viewmodel.messages.MessageListHeaderViewModel
import io.getstream.chat.android.ui.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.ui.viewmodel.messages.MessageListViewModelFactory
import io.getstream.chat.android.ui.viewmodel.messages.bindView
import io.getstream.log.StreamLog
import io.getstream.log.TaggedLogger
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
open class BaseChatFragment : BaseSenderFragment(R.layout.fragment_chat_message) {

    protected val chatArguments by navArgs<ChatMessageFragmentArgs>()

    protected lateinit var mBinding: FragmentChatMessageBinding

    protected lateinit var mChannelId: String

    protected var mMessageId: String? = null

    private val logger: TaggedLogger = StreamLog.getLogger("Chat:BaseChatFragment")

    protected val messageListViewModelFactory: MessageListViewModelFactory by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        MessageListViewModelFactory(
            context = requireContext().applicationContext,
            cid = mChannelId,
            messageId = mMessageId,
        )
    }

    protected val messageAudioViewModel: ChatGPTAudioViewModel by viewModels()

    protected val messageListViewModel: MessageListViewModel by viewModels { messageListViewModelFactory }

    protected val messageListHeaderViewModel: MessageListHeaderViewModel by viewModels { messageListViewModelFactory }

    protected val messageComposerViewModel: MessageComposerViewModel by viewModels { messageListViewModelFactory }

    protected lateinit var mChannel: Channel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChannelId = chatArguments.extraChannelId
        mMessageId = chatArguments.extraMessageId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentChatMessageBinding.bind(view)
        setupMessageComposerView()
        activity?.let {
            mBinding.root.postDelayed({
                setupMessageList()
                observerStateAndEvents()
            }, 500)
        }
    }

    private fun setupPlayerView() {

        /*with(mBinding.messageListHeaderView) {
            messageListHeaderViewModel.bindView(this, viewLifecycleOwner)
            setBackButtonClickListener {
//                messageListViewModel.onEvent(MessageListViewModel.Event.BackButtonPressed)
                popBackStack()
            }
        }*/
    }

    private fun setupMessageList() {
        with(mBinding.messageListView) {
            messageListViewModel.bindView(this, this@BaseChatFragment)
        }
    }

    protected open fun setupEmptyMessageView(messageListView: MessageListView) {
    }

    protected open fun setupMessageComposerView() {
    }

    @CallSuper
    override fun observerStateAndEvents() {
        if (activity == null) {
            return
        }
        super.observerStateAndEvents()
        /*messageSenderViewModel.typingState.observe(viewLifecycleOwner) { typingState ->
            when(typingState) {
                is ChatGPTMessageViewModel.TypingState.Typing -> {
                    mBinding.messageListHeaderView.showTypingStateLabel(listOf(typingState.user))
                }
                is ChatGPTMessageViewModel.TypingState.Normal -> {
                    mBinding.messageListHeaderView.showTypingStateLabel(listOf())
                }

            }
        }*/

        lifecycleScope.launch {

            messageAudioViewModel.textToSpeechState.collect { textToSpeechState ->
                logger.d { "textToSpeechState $textToSpeechState" }
                when (textToSpeechState) {
                    is ChatGPTAudioViewModel.TextToSpeechState.Success -> {
                        // mBinding.playViewContainer.isVisible = true
//                    MusicPlayerController.openQueue(listOf(textToSpeechState.audio), 0, true)
                        if (textToSpeechState.audio.file != null && textToSpeechState.audio.message != null) {
                            messageListViewModel.onEvent(
                                MessageListViewModel.Event.PlayMessageAudio(
                                    MessagePlayData(
                                        message = textToSpeechState.audio.message!!,
                                        textToSpeechState.audio.file!!
                                    )
                                )
                            )

                            // messageComposerViewModel.playAudio(textToSpeechState.audio.file!!) File("/storage/emulated/0/Download/tts_messaging_0c85724c-37ec-4e9e-b694-63cd6fc0f31d.mp3")
                        }
                    }

                    is ChatGPTAudioViewModel.TextToSpeechState.Fail -> {
                        // mBinding.playViewContainer.isVisible = false
//                    MusicPlayerController.pauseAudio()
                    }

                    else -> {
                    }
                }

            }
        }


        messageSenderViewModel.errorEvents.observe(viewLifecycleOwner) {
            requireContext().showToast(R.string.error_network_failure)
        }

        /*imageMessageSenderViewModel.typingState.observe(viewLifecycleOwner) {typingState ->
            when(typingState) {
                is ChatGPTImageViewModel.TypingState.Typing -> {
                    mBinding.messageListHeaderView.showTypingStateLabel(listOf(typingState.user))
                }
                is ChatGPTImageViewModel.TypingState.Normal -> {
                    mBinding.messageListHeaderView.showTypingStateLabel(listOf())
                }

            }
        }*/

        imageMessageSenderViewModel.errorEvents.observe(viewLifecycleOwner) {
            requireContext().showToast(R.string.error_network_failure)
        }

        messageListViewModel.channel.observe(viewLifecycleOwner) {
            mChannel = it
            setupEmptyMessageView(mBinding.messageListView)
        }
    }

    companion object {
        const val EXTRA_CHANNEL_ID = "extra_channel_id"
        const val EXTRA_MESSAGE_ID = "extra_message_id"

        fun createChatImageInstance(
            channelId: String,
            messageId: String? = null
        ): ChatImageFragment {
            val chatImageFragment: ChatImageFragment = ChatImageFragment()
            chatImageFragment.apply {
                arguments = ChatImageFragmentArgs(channelId, messageId).toBundle()
            }

            return chatImageFragment
        }

        fun createChatMessageInstance(
            channelId: String,
            messageId: String? = null
        ): ChatMessageFragment {
            val chatMessageFragment: ChatMessageFragment = ChatMessageFragment()
            chatMessageFragment.apply {
                arguments = ChatMessageFragmentArgs(channelId, messageId).toBundle()
            }

            return chatMessageFragment
        }
    }
}