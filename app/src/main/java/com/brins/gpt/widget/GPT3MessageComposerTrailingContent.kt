package com.brins.gpt.widget

import android.content.Context
import android.util.AttributeSet
import io.getstream.chat.android.ui.common.state.messages.composer.MessageComposerState
import io.getstream.chat.android.ui.feature.messages.composer.content.DefaultMessageComposerTrailingContent

class GPT3MessageComposerTrailingContent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, def: Int = 0
) : DefaultMessageComposerTrailingContent(context, attrs, def) {

    override fun renderState(state: MessageComposerState) {
        super.renderState(state)
        binding.recordAudioButton.visibility = GONE
        binding.recordAudioButton.isEnabled = false
    }
}