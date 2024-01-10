package com.brins.gpt.fragment

import android.os.Bundle
import android.view.View
import com.brins.gpt.R
import com.brins.gpt.widget.DallEMessageComposerLeadingContent
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.ui.common.state.messages.Edit
import io.getstream.chat.android.ui.common.state.messages.MessageMode
import io.getstream.chat.android.ui.common.state.messages.Reply
import io.getstream.chat.android.ui.viewmodel.messages.bindView
import io.getstream.log.StreamLog

@AndroidEntryPoint
class ChatImageFragment : BaseChatFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageMessageSenderViewModel.checkIsEmptyMessage(mChannelId)
    }

    override fun setupMessageComposerView() {
        mBinding.messageComposerView.setLeadingContent(DallEMessageComposerLeadingContent(requireContext()))

        messageComposerViewModel.apply {
            bindView(mBinding.messageComposerView, viewLifecycleOwner, sendMessageButtonClickListener = {
                    message ->
                messageComposerViewModel.sendMessage(message.copy(extraData = mChannel.extraData)){ sentMessage->
                    if (sentMessage.isSuccess) {
                        imageMessageSenderViewModel.sendStreamChatMessage(sentMessage.getOrNull()!!, mChannel,true)
                    }
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

    override fun observerStateAndEvents() {
        super.observerStateAndEvents()
        imageMessageSenderViewModel.isMessageEmpty.observe(viewLifecycleOwner) { isMessageEmpty ->
            if (isMessageEmpty) {
                StreamLog.d("emptyMessage") {
                    "isMessageEmpty: ${isMessageEmpty}"
                }
                imageMessageSenderViewModel.sendStreamChatMessage(mChannelId, getString(R.string.toast_dall_e_hello), false)
            }
        }


    }
}