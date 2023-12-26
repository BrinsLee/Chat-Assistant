package com.brins.gpt.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.brins.gpt.R
import com.brins.gpt.databinding.FragmentChatMessageBinding
import com.brins.gpt.viewmodel.ChatGPTMessageViewModel
import com.brins.lib_base.base.BaseFragment
import com.brins.lib_base.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.ui.common.state.messages.Edit
import io.getstream.chat.android.ui.common.state.messages.MessageMode
import io.getstream.chat.android.ui.common.state.messages.Reply
import io.getstream.chat.android.ui.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.ui.viewmodel.messages.MessageListHeaderViewModel
import io.getstream.chat.android.ui.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.ui.viewmodel.messages.MessageListViewModelFactory
import io.getstream.chat.android.ui.viewmodel.messages.bindView
import javax.inject.Inject


@AndroidEntryPoint
class ChatMessageFragment : BaseFragment(R.layout.fragment_chat_message) {

    private val arguments by navArgs<ChatMessageFragmentArgs>()

    private lateinit var mBinding: FragmentChatMessageBinding

    private lateinit var mChannelId: String

    private var mMessageId: String? = null

    private val factory: MessageListViewModelFactory by lazy(LazyThreadSafetyMode.NONE) {
        MessageListViewModelFactory(
            context = requireContext().applicationContext,
            cid = mChannelId,
            messageId = mMessageId,
        )
    }

    private val messageSenderViewModel: ChatGPTMessageViewModel by viewModels()

    private val messageListViewModel: MessageListViewModel by viewModels { factory }

    private val messageListHeaderViewModel: MessageListHeaderViewModel by viewModels { factory }

    private val messageComposerViewModel: MessageComposerViewModel by viewModels { factory }

    private lateinit var mChannel: Channel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChannelId = arguments.extraChannelId
        mMessageId = arguments.extraMessageId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentChatMessageBinding.bind(view)
        setUpMessageListHeader()
        setUpMessageList()
        setUpMessageComposerView()
        observerStateAndEvents()
        messageSenderViewModel.checkIsEmptyMessage(mChannelId)
    }

    private fun setUpMessageComposerView() {
        messageComposerViewModel.apply {
            bindView(mBinding.messageComposerView, viewLifecycleOwner, sendMessageButtonClickListener = {
                message ->
                messageComposerViewModel.sendMessage(message){
                    messageSenderViewModel.sendStreamChatMessage(message, mChannel,true)
                }

            })
            messageListViewModel.mode.observe(viewLifecycleOwner) {
                when(it) {
                    is MessageMode.MessageThread -> {
//                        headerViewModel.setActiveThread(it.parentMessage)
                        messageComposerViewModel.setMessageMode(MessageMode.MessageThread(it.parentMessage))
                    }
                    is MessageMode.Normal -> {
                        messageComposerViewModel.leaveThread()
                    }
                }
            }
            mBinding.messageListView.setMessageReplyHandler { _, message ->
                messageComposerViewModel.performMessageAction(Reply(message))
            }
            mBinding.messageListView.setMessageEditHandler { message ->
                messageComposerViewModel.performMessageAction(Edit(message))
            }
            mBinding.messageListView.setAttachmentReplyOptionClickHandler { result ->
                messageListViewModel.getMessageById(result.messageId)?.let { message ->
                    messageComposerViewModel.performMessageAction(Reply(message))
                }
            }
        }
    }

    private fun setUpMessageList() {
        with(mBinding.messageListView) {
          messageListViewModel.bindView(this, viewLifecycleOwner)
        }
    }

    private fun setUpMessageListHeader() {
        with(mBinding.messageListHeaderView) {
            messageListHeaderViewModel.bindView(this, viewLifecycleOwner)
            setBackButtonClickListener {
//                messageListViewModel.onEvent(MessageListViewModel.Event.BackButtonPressed)
                popBackStack()
            }
        }
    }

    private fun observerStateAndEvents() {
        messageSenderViewModel.isMessageEmpty.observe(viewLifecycleOwner) { isMessageEmpty ->
            if (isMessageEmpty) {
                messageSenderViewModel.sendStreamChatMessage(mChannelId, getString(R.string.toast_hello), false)
            }
        }

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

        messageListViewModel.channel.observe(viewLifecycleOwner) {
            mChannel = it
        }

        messageSenderViewModel.errorEvents.observe(viewLifecycleOwner) {
            requireContext().showToast(R.string.error_network_failure)
        }
    }

    companion object {
        const val EXTRA_CHANNEL_ID = "extra_channel_id"
        const val EXTRA_MESSAGE_ID = "extra_message_id"
    }
}