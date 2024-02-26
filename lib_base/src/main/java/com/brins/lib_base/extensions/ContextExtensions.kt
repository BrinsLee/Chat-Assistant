package com.brins.lib_base.extensions

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.brins.lib_base.utils.ATHUtil
import io.getstream.chat.android.ui.R
import io.getstream.chat.android.ui.utils.extensions.getDrawableCompat


fun Context.surfaceColor() = resolveColor(com.google.android.material.R.attr.colorSurface, getColor(com.brins.lib_base.R.color.white))

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

fun Context.showToast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    showToast(getString(stringRes), duration)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

public fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}