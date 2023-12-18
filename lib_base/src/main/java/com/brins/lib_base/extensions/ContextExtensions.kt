package com.brins.lib_base.extensions

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.ContextThemeWrapper
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment
import com.brins.lib_base.utils.ATHUtil
import io.getstream.chat.android.ui.R


fun Context.surfaceColor() = resolveColor(com.google.android.material.R.attr.colorSurface, Color.WHITE)

fun Context.resolveColor(@AttrRes attr: Int, fallBackColor: Int = 0) =
    ATHUtil.resolveColor(this, attr, fallBackColor)

fun Fragment.resolveColor(@AttrRes attr: Int, fallBackColor: Int = 0) =
    ATHUtil.resolveColor(requireContext(), attr, fallBackColor)

fun Dialog.resolveColor(@AttrRes attr: Int, fallBackColor: Int = 0) =
    ATHUtil.resolveColor(context, attr, fallBackColor)

internal fun Context.createThemeWrapper(): Context {
    val typedValue = TypedValue()
    return when {
        theme.resolveAttribute(R.attr.streamUiValidTheme, typedValue, true) -> this
        theme.resolveAttribute(R.attr.streamUiTheme, typedValue, true) -> ContextThemeWrapper(this, typedValue.resourceId)
        else -> ContextThemeWrapper(this, R.style.StreamUiTheme)
    }
}