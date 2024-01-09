package com.brins.gpt.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.brins.lib_base.extensions.isVisible
import io.getstream.chat.android.ui.common.state.messages.composer.MessageComposerState
import io.getstream.chat.android.ui.feature.messages.composer.content.DefaultMessageComposerLeadingContent
import io.getstream.chat.android.ui.feature.messages.composer.content.DefaultMessageComposerTrailingContent

class GPT4MessageComposerTrailingContent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, def: Int = 0
) : DefaultMessageComposerTrailingContent(context, attrs, def) {

    override fun renderState(state: MessageComposerState) {
        super.renderState(state)
        binding.recordAudioButton.visibility = VISIBLE
        binding.recordAudioButton.isEnabled = true
    }
}