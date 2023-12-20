package com.brins.gpt.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.brins.gpt.R
import com.brins.gpt.databinding.FragmentChatMessageBinding
import com.brins.lib_base.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.core.internal.exhaustive


@AndroidEntryPoint
class ChatMessageFragment : BaseFragment(R.layout.fragment_chat_message) {

    private val arguments by navArgs<ChatMessageFragmentArgs>()

    private lateinit var mBinding: FragmentChatMessageBinding

    private lateinit var mChannelId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentChatMessageBinding.bind(view)
        mChannelId = arguments.extraChannelId

    }

    companion object {

    }
}