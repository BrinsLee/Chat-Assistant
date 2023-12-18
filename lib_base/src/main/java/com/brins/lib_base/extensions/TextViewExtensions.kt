package com.brins.lib_base.extensions

import android.widget.TextView
import io.getstream.chat.android.ui.font.TextStyle

fun TextView.setTextStyle(textStyle: TextStyle) {
    textStyle.apply(this)
}