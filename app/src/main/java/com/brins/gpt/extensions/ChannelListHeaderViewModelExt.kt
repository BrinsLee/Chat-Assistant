package com.brins.gpt.extensions

import androidx.lifecycle.LifecycleOwner
import com.brins.lib_base.widgets.ChannelListHeaderView
import io.getstream.chat.android.models.ConnectionState
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListHeaderViewModel

fun ChannelListHeaderViewModel.bindsView(view: ChannelListHeaderView, lifecycleOwner: LifecycleOwner) {
    with(view) {
        currentUser.observe(lifecycleOwner) { user ->
            user?.let(::setUser)
        }
        connectionState.observe(lifecycleOwner) { connectionState ->
            when (connectionState) {
                is ConnectionState.Connected -> hideOfflineTitle()
                is ConnectionState.Connecting -> showConnectingTitle()
                is ConnectionState.Offline -> showOfflineTitle()
            }
        }
    }
}