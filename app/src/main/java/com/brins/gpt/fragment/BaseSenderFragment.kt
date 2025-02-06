package com.brins.gpt.fragment

import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import com.brins.gpt.MainActivity
import com.brins.gpt.viewmodel.ChatGPTImageViewModel
import com.brins.gpt.viewmodel.ChatGPTMessageViewModel
import com.brins.lib_base.base.AbsMusicServiceFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseSenderFragment(@LayoutRes layout: Int): AbsMusicServiceFragment(layout) {

    protected lateinit var imageMessageSenderViewModel: ChatGPTImageViewModel

    protected lateinit var messageSenderViewModel: ChatGPTMessageViewModel

    @CallSuper
    protected open fun observerStateAndEvents() {
        messageSenderViewModel = (requireActivity() as MainActivity).getMessageSenderViewModel()
        imageMessageSenderViewModel = (requireActivity() as MainActivity).getImageMessageSenderViewModel()
    }
}