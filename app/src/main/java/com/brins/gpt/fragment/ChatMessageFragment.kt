package com.brins.gpt.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brins.gpt.R
import com.brins.gpt.databinding.FragmentChatMessageBinding
import com.brins.gpt.viewmodel.ChatGPTMessageViewModel
import com.brins.lib_base.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.ui.viewmodel.messages.MessageListHeaderViewModel
import io.getstream.chat.android.ui.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.ui.viewmodel.messages.MessageListViewModelFactory
import io.getstream.chat.android.ui.viewmodel.messages.bindView


@AndroidEntryPoint
class ChatMessageFragment : BaseFragment(R.layout.fragment_chat_message) {

    private val arguments by navArgs<ChatMessageFragmentArgs>()

    private lateinit var mBinding: FragmentChatMessageBinding

    private lateinit var mChannelId: String

    private val factory: MessageListViewModelFactory by lazy(LazyThreadSafetyMode.NONE) {
        MessageListViewModelFactory(
            context = requireContext().applicationContext,
            cid = mChannelId,
            messageId = null,
        )
    }

    private val mViewModel: ChatGPTMessageViewModel by viewModels()

    private val messageListViewModel: MessageListViewModel by viewModels { factory }

    private val messageListHeaderViewModel: MessageListHeaderViewModel by viewModels { factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mChannelId = arguments.extraChannelId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentChatMessageBinding.bind(view)
        setUpMessageListHeader()
        setUpMessageList()
        observerStateAndEvents()
        mViewModel.checkIsEmptyMessage(mChannelId)
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
        mViewModel.isMessageEmpty.observe(viewLifecycleOwner) { isMessageEmpty ->
            if (isMessageEmpty) {
                mViewModel.sendStreamChatMessage(mChannelId, getString(R.string.toast_hello))
            }
        }
    }

    companion object {
        const val EXTRA_CHANNEL_ID = "extra_channel_id"
    }
}