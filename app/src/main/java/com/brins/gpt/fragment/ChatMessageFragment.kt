package com.brins.gpt.fragment

import android.os.Bundle
import android.view.View
import com.brins.gpt.databinding.CommonEmptyMessageViewBinding
import com.brins.gpt.widget.GPT3MessageComposerLeadingContent
import com.brins.gpt.widget.GPT3MessageComposerTrailingContent
import com.brins.gpt.widget.GPT4MessageComposerLeadingContent
import com.brins.gpt.widget.GPT4MessageComposerTrailingContent
import com.brins.lib_base.extensions.getChannelIcon
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.ui.common.state.messages.MessageMode
import io.getstream.chat.android.ui.common.state.messages.Reply
import io.getstream.chat.android.ui.feature.messages.list.MessageListView
import io.getstream.chat.android.ui.viewmodel.messages.bindView


@AndroidEntryPoint
class ChatMessageFragment : BaseChatFragment() {

    /*private val arguments by navArgs<ChatMessageFragmentArgs>()

    private lateinit var mBinding: FragmentChatMessageBinding

    private lateinit var mChannelId: String

    private var mMessageId: String? = null

    private val messageListViewModelFactory: MessageListViewModelFactory by lazy(LazyThreadSafetyMode.NONE) {
        MessageListViewModelFactory(
            context = requireContext().applicationContext,
            cid = mChannelId,
            messageId = mMessageId,
        )
    }

//    private val messageSenderViewModel: ChatGPTMessageViewModel by viewModels()

    private val messageSenderViewModel: ChatGPTMessageViewModel by lazy { (requireActivity() as MainActivity).getMessageSenderViewModel()  }

    private val messageListViewModel: MessageListViewModel by viewModels { messageListViewModelFactory }

    private val messageListHeaderViewModel: MessageListHeaderViewModel by viewModels { messageListViewModelFactory }

    private val messageComposerViewModel: MessageComposerViewModel by viewModels { messageListViewModelFactory }

    private lateinit var mChannel: Channel*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)/*mChannelId = arguments.extraChannelId
        mMessageId = arguments.extraMessageId*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mBinding = FragmentChatMessageBinding.bind(view)
//        setUpMessageListHeader()
//        setUpMessageList()
//        setUpMessageComposerView()
//        observerStateAndEvents()
//        messageSenderViewModel.checkIsEmptyMessage(mChannelId)
    }

    override fun setupEmptyMessageView(messageListView: MessageListView) {
        val binding: CommonEmptyMessageViewBinding =
            CommonEmptyMessageViewBinding.inflate(layoutInflater)
        binding.userAvatarView.setImageResource(mChannel.getChannelIcon())
        messageListView.setEmptyStateView(binding.root)
    }

    override fun setupMessageComposerView() {
        // todo 根据是否付费判断是否展示附件按钮
        if (false) {
            mBinding.messageComposerView.setLeadingContent(
                GPT3MessageComposerLeadingContent(
                    requireContext()
                )
            )
            mBinding.messageComposerView.setTrailingContent(
                GPT3MessageComposerTrailingContent(
                    requireContext()
                )
            )
        } else {
            mBinding.messageComposerView.setLeadingContent(
                GPT4MessageComposerLeadingContent(
                    requireContext()
                )
            )
            mBinding.messageComposerView.setTrailingContent(
                GPT4MessageComposerTrailingContent(
                    requireContext()
                )
            )
        }
        messageComposerViewModel.apply {
            bindView(
                mBinding.messageComposerView,
                viewLifecycleOwner,
                sendMessageButtonClickListener = { message ->
                    // 自己发送的信息
                    messageComposerViewModel.sendMessage(message.copy(extraData = mChannel.extraData)) { sentMessage ->
                        if (sentMessage.isSuccess) {
                            //发送成功后调用openai
                            messageSenderViewModel.sendStreamChatMessage(
                                sentMessage.getOrNull()!!,
                                mChannel,
                                true
                            )
                        }
                    }

                })
            messageListViewModel.mode.observe(viewLifecycleOwner) {
                when (it) {
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

            mBinding.messageListView.setMessageTextToSpeechHandler { message ->
                messageAudioViewModel.textToSpeech(requireContext(), message)
            }

            mBinding.messageListView.setAttachmentReplyOptionClickHandler { result ->
                messageListViewModel.getMessageById(result.messageId)?.let { message ->
                    messageComposerViewModel.performMessageAction(Reply(message))
                }
            }
        }
    }

    /*    private fun setUpMessageList() {
            with(mBinding.messageListView) {
              messageListViewModel.bindView(this, viewLifecycleOwner)
            }
        }*/

    /*private fun setUpMessageListHeader() {
        with(mBinding.messageListHeaderView) {
            messageListHeaderViewModel.bindView(this, viewLifecycleOwner)
            setBackButtonClickListener {
//                messageListViewModel.onEvent(MessageListViewModel.Event.BackButtonPressed)
                popBackStack()
            }
        }
    }*/

    override fun observerStateAndEvents() {
        if (activity == null) {
            return
        }
        super.observerStateAndEvents()
        messageSenderViewModel.isMessageEmpty.observe(viewLifecycleOwner) { isMessageEmpty ->
            /*if (isMessageEmpty) {
                messageSenderViewModel.sendStreamChatMessage(mChannelId, getString(R.string.toast_hello), false)
            }*/
        }


    }


}