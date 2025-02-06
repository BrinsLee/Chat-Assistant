package com.brins.lib_base.widgets

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import androidx.core.view.isVisible
import com.brins.lib_base.R
import com.brins.lib_base.databinding.LayoutChannelListHeaderBinding
import com.brins.lib_base.extensions.ThemeInflater
import com.brins.lib_base.extensions.setTextStyle
import io.getstream.chat.android.models.User
import io.getstream.chat.android.ui.font.TextStyle

class ChannelListHeaderView @JvmOverloads constructor(
    context: Context, attr: AttributeSet? = null, def: Int = 0
) : ConstraintLayout(context, attr, def) {

    private val binding = LayoutChannelListHeaderBinding.inflate(ThemeInflater, this, true)

    init {
        initHeaderView(attr)
    }

    private fun initHeaderView(attr: AttributeSet?) {
        context.obtainStyledAttributes(
            attr,
            R.styleable.ChannelListHeaderView,
            R.attr.UiChannelListHeaderStyle,
            R.style.Ui_ChannelListHeader
        ).use { typedArray ->
            configToolBar()
            configUserAvatar(typedArray)
            configOfflineTitle(typedArray)
            configureSeparator(typedArray)
        }
    }

    /**
     * 设置分割线
     */
    private fun configureSeparator(typedArray: TypedArray) {
        binding.separator.apply {
            val drawable =
                typedArray.getDrawable(R.styleable.ChannelListHeaderView_UiChannelListSeparatorBackgroundDrawable)
            visibility = if (drawable != null) VISIBLE else GONE
            background = drawable
        }
    }

    /**
     * 配置离线标题
     */
    private fun configOfflineTitle(typedArray: TypedArray) {
        binding.offlineTextView.setTextStyle(getOfflineTitleTextStyle(typedArray))
        binding.offlineProgressBar.apply {
            isVisible =
                typedArray.getBoolean(R.styleable.ChannelListHeaderView_UiShowOfflineProgressBar, true)
            indeterminateTintList = getProgressBarTint(typedArray)
        }
    }

    /**
     * 配置用户头像
     */
    private fun configUserAvatar(typedArray: TypedArray) {

    }

    /**
     * 配置toolBar
     */
    private fun configToolBar() {
        with(binding.toolbar) {
            /*val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, requireContext().resources.getDimension(R.dimen.spacing_50dp).toInt())
            lp.setMargins(0,getStatusBarHeight(requireContext()),0,0)
            layoutParams = lp*/

            inflateMenu(R.menu.home_fragment_menu)
            /*setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    return onMenuItemClickListener?.onMenuItemClick(item)?:false
                }

            })*/
        }
    }

    private fun getOfflineTitleTextStyle(typedArray: TypedArray): TextStyle {
        return TextStyle.Builder(typedArray)
            .size(
                R.styleable.ChannelListHeaderView_UiOfflineTitleTextSize,
                context.resources.getDimension(io.getstream.chat.android.ui.R.dimen.stream_ui_text_large)
                    .toInt(),
            )
            .color(
                R.styleable.ChannelListHeaderView_UiOfflineTitleTextColor,
                ContextCompat.getColor(context, io.getstream.chat.android.ui.R.color.stream_ui_text_color_primary),
            )
            .font(
                io.getstream.chat.android.ui.R.styleable.ChannelListHeaderView_streamUiOfflineTitleFontAssets,
                io.getstream.chat.android.ui.R.styleable.ChannelListHeaderView_streamUiOfflineTitleTextFont,
            )
            .style(
                io.getstream.chat.android.ui.R.styleable.ChannelListHeaderView_streamUiOfflineTitleTextStyle,
                Typeface.BOLD,
            ).build()
    }

    private fun getProgressBarTint(typedArray: TypedArray) =
        typedArray.getColorStateList(R.styleable.ChannelListHeaderView_UiOfflineProgressBarTint)
            ?: ContextCompat.getColorStateList(context, R.color.color_178CE9)

    fun setUser(user: User) {
        binding.userAvatarView.setUser(user)
    }

    fun showOnlineTitle(string: String) {
        binding.onlineTextView.text = string
    }

    fun showTypingView() {
        binding.typing.isVisible = true
    }

    fun hideTypingView() {
        binding.typing.isVisible = false
    }

    fun hideOfflineTitle() {
        binding.offlineTitleContainer.isVisible = false
        binding.onlineTitleContainer.isVisible = true
    }

    fun showOfflineTitle() {
        binding.offlineTitleContainer.isVisible = true
        binding.onlineTitleContainer.isVisible = false
        binding.offlineProgressBar.isVisible = false
        binding.offlineTextView.isVisible = true
        binding.offlineTextView.text = context.getString(R.string.disconnected)
    }

    fun showConnectingTitle() {
        binding.offlineTitleContainer.isVisible = true
        binding.onlineTitleContainer.isVisible = false
        binding.offlineProgressBar.isVisible = true

        binding.offlineTextView.text = resources.getString(R.string.waiting_for_network)
    }

    fun setOnMenuItemClickListener(onMenuItemClickListener: OnMenuItemClickListener) {
        binding.toolbar.setOnMenuItemClickListener(onMenuItemClickListener)
    }

    fun setOnUserAvatarClickListener(listener: () -> Unit) {
        binding.userAvatarView.setOnClickListener {
            listener.invoke()
        }
    }
}