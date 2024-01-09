package com.brins.gpt.widget

import android.content.Context
import android.util.AttributeSet
import io.getstream.chat.android.ui.common.state.messages.composer.MessageComposerState
import io.getstream.chat.android.ui.feature.messages.composer.content.DefaultMessageComposerLeadingContent

class GPT3MessageComposerLeadingContent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, def: Int = 0
) : DefaultMessageComposerLeadingContent(context, attrs, def) {

    override fun renderState(state: MessageComposerState) {
        super.renderState(state)
        binding.attachmentsButton.isEnabled = false
        binding.commandsButton.visibility = GONE
    }
}