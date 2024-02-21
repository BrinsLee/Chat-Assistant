package com.brins.gpt.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.brins.gpt.MainActivity
import com.brins.gpt.R
import com.brins.gpt.databinding.FragmentChatMessageBinding
import com.brins.gpt.viewmodel.ChatGPTImageViewModel
import com.brins.gpt.viewmodel.ChatGPTMessageViewModel
import com.brins.lib_base.base.BaseFragment
import com.brins.lib_base.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.ui.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.ui.viewmodel.messages.MessageListHeaderViewModel
import io.getstream.chat.android.ui.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.ui.viewmodel.messages.MessageListViewModelFactory
import io.getstream.chat.android.ui.viewmodel.messages.bindView

@AndroidEntryPoint
open class BaseChatFragment: BaseFragment(R.layout.fragment_chat_message) {

    protected val arguments by navArgs<ChatMessageFragmentArgs>()

    protected lateinit var mBinding: FragmentChatMessageBinding

    protected lateinit var mChannelId: String

    protected var mMessageId: String? = null

    protected val messageListViewModelFactory: MessageListViewModelFactory by lazy(LazyThreadSafetyMode.NONE) {
        MessageListViewModelFactory(
            context = requireContext().applicationContext,
            cid = mChannelId,
            messageId = mMessageId,
        )
    }

//    private val messageSenderViewModel: ChatGPTMessageViewModel by viewModels()
    protected val imageMessageSenderViewModel: ChatGPTImageViewModel by lazy { (requireActivity() as MainActivity).getImageMessageSenderViewModel() }

    protected val messageSenderViewModel: ChatGPTMessageViewModel by lazy { (requireActivity() as MainActivity).getMessageSenderViewModel()  }

    protected val messageListViewModel: MessageListViewModel by viewModels { messageListViewModelFactory }

    protected val messageListHeaderViewModel: MessageListHeaderViewModel by viewModels { messageListViewModelFactory }

    protected val messageComposerViewModel: MessageComposerViewModel by viewModels { messageListViewModelFactory }

    protected lateinit var mChannel: Channel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChannelId = arguments.extraChannelId
        mMessageId = arguments.extraMessageId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentChatMessageBinding.bind(view)
        setupMessageListHeader()
        setupMessageComposerView()
        mBinding.root.postDelayed({
            setupMessageList()
            observerStateAndEvents()
        }, 500)

    }

    private fun setupMessageListHeader() {
        with(mBinding.messageListHeaderView) {
            messageListHeaderViewModel.bindView(this, viewLifecycleOwner)
            setBackButtonClickListener {
//                messageListViewModel.onEvent(MessageListViewModel.Event.BackButtonPressed)
                popBackStack()
            }
        }
    }

    private fun setupMessageList() {
        with(mBinding.messageListView) {
            messageListViewModel.bindView(this, viewLifecycleOwner)
        }
    }

    protected open fun setupMessageComposerView() {

    }

    @CallSuper
    protected open fun observerStateAndEvents() {
        messageSenderViewModel.typingState.observe(viewLifecycleOwner) { typingState ->
            when(typingState) {
                is ChatGPTMessageViewModel.TypingState.Typing -> {
                    mBinding.messageListHeaderView.showTypingStateLabel(listOf(typingState.user))
                }
                is ChatGPTMessageViewModel.TypingState.Normal -> {
                    mBinding.messageListHeaderView.showTypingStateLabel(listOf())
                }

            }
        }
        messageSenderViewModel.errorEvents.observe(viewLifecycleOwner) {
            requireContext().showToast(R.string.error_network_failure)
        }

        imageMessageSenderViewModel.typingState.observe(viewLifecycleOwner) {typingState ->
            when(typingState) {
                is ChatGPTImageViewModel.TypingState.Typing -> {
                    mBinding.messageListHeaderView.showTypingStateLabel(listOf(typingState.user))
                }
                is ChatGPTImageViewModel.TypingState.Normal -> {
                    mBinding.messageListHeaderView.showTypingStateLabel(listOf())
                }

            }
        }

        imageMessageSenderViewModel.errorEvents.observe(viewLifecycleOwner) {
            requireContext().showToast(R.string.error_network_failure)
        }

        messageListViewModel.channel.observe(viewLifecycleOwner) {
            mChannel = it
        }
    }

    companion object {
        const val EXTRA_CHANNEL_ID = "extra_channel_id"
        const val EXTRA_MESSAGE_ID = "extra_message_id"
    }
}